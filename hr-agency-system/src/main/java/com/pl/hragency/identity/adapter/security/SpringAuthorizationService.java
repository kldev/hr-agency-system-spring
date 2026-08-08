package com.pl.hragency.identity.adapter.security;

import com.pl.hragency.identity.application.port.AuthorizationService;
import com.pl.hragency.identity.domain.model.UserRole;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SpringAuthorizationService implements AuthorizationService {
    @Override
    public void requireRole(UserRole role) {
        if (!hasRole(role)) {
            throw new AccessDeniedException(
                    "Required role: " + role);
        }
    }

    @Override
    public void requireAnyRole(UserRole... roles) {

        for (UserRole role : roles) {
            if (hasRole(role)) {
                return;
            }
        }

        throw new AccessDeniedException("Access denied.");
    }

    @Override
    public boolean hasRole(UserRole role) {

        String authority = "ROLE_" + role.name();

        return authentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(authority));
    }

    private Authentication authentication() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AccessDeniedException("No authentication.");
        }

        return authentication;
    }
}
