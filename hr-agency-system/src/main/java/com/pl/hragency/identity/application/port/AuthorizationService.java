package com.pl.hragency.identity.application.port;

import com.pl.hragency.identity.domain.model.OrganizationRole;

public interface AuthorizationService {
    boolean hasRole(OrganizationRole role);

    void requireRole(OrganizationRole role);

    void requireAnyRole(OrganizationRole...roles);
}

