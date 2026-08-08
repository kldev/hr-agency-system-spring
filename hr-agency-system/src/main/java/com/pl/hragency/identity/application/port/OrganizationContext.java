package com.pl.hragency.identity.application.port;

import com.pl.hragency.identity.domain.model.UserOrganizationId;

public interface OrganizationContext {

    UserOrganizationId getRequiredOrganizationId();

    void setOrganizationId(UserOrganizationId organizationId);

    void clear();
}
