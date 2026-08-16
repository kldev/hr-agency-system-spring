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

    public JwtAuthenticationFilter(
            JwtService jwtService
           ) {
        this.jwtService = jwtService;
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


        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            JwtSubjectType subjectType = jwtService.extractSubjectType(token);

            switch (subjectType) {
                case ORGANIZATION -> setAuthentication(jwtService.extractSecurityUser(token));
                case PLATFORM -> setAuthentication(jwtService.extractPlatformOwner(token));
            }
        }
        catch (Exception ex) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(
            org.springframework.security.core.userdetails.UserDetails user
    ) {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return isPublicEndpoint(path);
    }

    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/actuator/health")
                | path.startsWith("/public/")
                || path.startsWith("/api/public/")
                || path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs");
    }
}


