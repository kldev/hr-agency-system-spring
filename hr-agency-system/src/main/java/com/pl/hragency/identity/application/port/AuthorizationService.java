package com.pl.hragency.identity.application.port;

import com.pl.hragency.identity.domain.model.UserRole;

public interface AuthorizationService {
    boolean hasRole(UserRole role);

    void requireRole(UserRole role);

    void requireAnyRole(UserRole ...roles);
}

