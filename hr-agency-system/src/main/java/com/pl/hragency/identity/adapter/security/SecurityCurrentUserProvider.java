package com.pl.hragency.identity.adapter.security;
import com.pl.hragency.identity.api.CurrentIntegrationClient;
import com.pl.hragency.identity.application.port.CurrentPrincipalProvider;
import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.application.security.IntegrationAuthentication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityCurrentUserProvider implements CurrentPrincipalProvider {
    @Override
    public CurrentUser getRequiredUser() {
        Authentication authentication = getAuthentication();

        if (!(authentication.getPrincipal() instanceof SecurityUser user)) {
            throw new AccessDeniedException("Organization user authentication required");
        }

        return new CurrentUser(
                user.userId(),
                user.orgId(),
                user.fullName(),
                user.roles().getFirst()
        );
    }

    @Override
    public CurrentIntegrationClient getRequiredIntegration() {
        Authentication authentication = getAuthentication();

        if (!(authentication.getPrincipal() instanceof IntegrationAuthentication integration)) {
            throw new AccessDeniedException(
                    "Integration client authentication required"
            );
        }

        return new CurrentIntegrationClient(
                integration.clientId(),
                integration.organizationId(),
                integration.getName(),
                integration.organizationUserId()
        );
    }

    private Authentication getAuthentication() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("Authentication required");
        }

        return authentication;
    }

}
