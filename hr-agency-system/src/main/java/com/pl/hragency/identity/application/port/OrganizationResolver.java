package com.pl.hragency.identity.application.port;

import com.pl.hragency.identity.domain.model.UserOrganizationId;

public interface OrganizationResolver {
    UserOrganizationId resolve(String slug);
}
