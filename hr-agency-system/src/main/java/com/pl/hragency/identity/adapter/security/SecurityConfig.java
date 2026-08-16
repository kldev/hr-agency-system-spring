package com.pl.hragency.identity.adapter.security;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    public SecurityConfig() {
    }

    @Bean
    UserDetailsService emptyDetailsService() {
        return username -> { throw new UsernameNotFoundException("no local users, only JWT tokens allowed"); };
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtFilter,
                                                   ApiKeyAuthenticationFilter  apiKeyFilter)
            throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                        )
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/platform/login").permitAll()
                        .requestMatchers("/docs")
                        .permitAll()
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/health/**",
                                "/api-spec/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/integrations/job-applications")
                            .hasAuthority("SCOPE_APPLICATION_WRITE")
                        .requestMatchers(HttpMethod.GET,"/api/public/feeds/**").permitAll()
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest()
                        .authenticated())
                //.httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(apiKeyFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {

                            response.setStatus(
                                    HttpServletResponse.SC_FORBIDDEN
                            );

                            response.setContentType(
                                    MediaType.APPLICATION_JSON_VALUE
                            );

                            response.getWriter().write(
                                    """
                                    {"message":"Access denied"}
                                    """
                            );
                        })
                )
                .build();
    }
}
