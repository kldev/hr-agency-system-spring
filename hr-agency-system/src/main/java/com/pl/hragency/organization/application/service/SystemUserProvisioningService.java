package com.pl.hragency.organization.application.service;


import com.pl.hragency.constants.SystemAccountNames;
import com.pl.hragency.identity.api.IdentityApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SystemUserProvisioningService {
    private final IdentityApi api;

    public SystemUserProvisioningService(IdentityApi api) {
        this.api = api;
    }

    @Transactional
    public void createAccounts(UUID organizationId) {
        if (!api.existsInOrganization(SystemAccountNames.SYSTEM, organizationId)) {
            api.createUser(SystemAccountNames.SYSTEM, "System", "-", "SYSTEM", organizationId, UUID.randomUUID().toString());
        }

        if (!api.existsInOrganization(SystemAccountNames.INTEGRATIONS, organizationId)) {
            api.createUser(SystemAccountNames.INTEGRATIONS, "INTEGRATIONS", "-", "SYSTEM", organizationId, UUID.randomUUID().toString());
        }
    }
}
