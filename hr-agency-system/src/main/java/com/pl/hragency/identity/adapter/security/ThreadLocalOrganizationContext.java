package com.pl.hragency.identity.adapter.security;

import com.pl.hragency.identity.application.port.OrganizationContext;
import com.pl.hragency.identity.domain.model.UserOrganizationId;
import org.springframework.stereotype.Component;

@Component
public class ThreadLocalOrganizationContext
        implements OrganizationContext {

    private final ThreadLocal<UserOrganizationId> currentOrganization =
            new ThreadLocal<>();

    @Override
    public UserOrganizationId getRequiredOrganizationId() {

        var organizationId = currentOrganization.get();

        if (organizationId == null) {
            throw new IllegalStateException(
                    "Organization context is not available"
            );
        }

        return organizationId;
    }

    @Override
    public void setOrganizationId(
            UserOrganizationId organizationId) {

        if (organizationId == null) {
            throw new IllegalArgumentException(
                    "Organization ID cannot be null"
            );
        }

        currentOrganization.set(organizationId);
    }

    @Override
    public void clear() {
        currentOrganization.remove();
    }
}
