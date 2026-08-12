package com.pl.hragency.identity.adapter.security;

import com.pl.hragency.identity.application.security.IntegrationClientAuthenticator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class ApiKeyAuthenticationFilter
        extends OncePerRequestFilter {

    private final IntegrationClientAuthenticator authenticator;

    public ApiKeyAuthenticationFilter(
            IntegrationClientAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey = request.getHeader("X-API-Key");

        if (apiKey != null && !apiKey.isBlank()) {

            var authentication =
                    authenticator.authenticate(apiKey);

            if (authentication != null) {
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
