package com.pl.hragency.identity.adapter.security;
import com.pl.hragency.identity.application.port.CurrentUserProvider;
import com.pl.hragency.identity.api.CurrentUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityCurrentUserProvider implements CurrentUserProvider {
    @Override
    public CurrentUser get() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        if (!(authentication.getPrincipal() instanceof SecurityUser user)) {
            return null;
        }

        return new CurrentUser(
                user.userId(),
                user.orgId(),
                user.fullName(),
                user.roles().getFirst()
        );
    }
}
