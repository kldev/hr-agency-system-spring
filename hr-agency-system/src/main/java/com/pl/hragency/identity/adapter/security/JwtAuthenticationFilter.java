package com.pl.hragency.identity.adapter.security;

import com.pl.hragency.identity.application.port.OrganizationContext;
import com.pl.hragency.identity.domain.model.UserOrganizationId;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;


@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final AppUserDetailsService userDetailsService;
    private final OrganizationContext organizationContext;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            AppUserDetailsService userDetailsService, OrganizationContext organizationContext) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.organizationContext = organizationContext;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)

            throws ServletException, IOException {


        String header =
                request.getHeader("Authorization");


        if(header == null ||
                !header.startsWith("Bearer ")) {

            filterChain.doFilter(request,response);
            return;
        }

        String token =
                header.substring(7);

        String username =
                jwtService.extractUsername(token);

        UUID organizationId =
                jwtService.extractOrganizationId(token);

        if(username != null &&
                SecurityContextHolder.getContext()
                        .getAuthentication() == null) {


            organizationContext.setOrganizationId(
                    new UserOrganizationId(organizationId)
            );

            SecurityUser user =(SecurityUser)
                    userDetailsService
                            .loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );


            SecurityContextHolder
                    .getContext()
                    .setAuthentication(auth);
        }

        filterChain.doFilter(request,response);
    }
}


