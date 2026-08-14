package com.pl.hragency.organization.application.handler;

import com.pl.hragency.organization.application.service.SystemUserProvisioningService;
import com.pl.hragency.organization.domain.event.OrganizationCreatedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class OrganizationCreatedEventHandler {
    private final SystemUserProvisioningService service;

    public OrganizationCreatedEventHandler(SystemUserProvisioningService service) {
        this.service = service;
    }

    @ApplicationModuleListener
    public void handleEvent(OrganizationCreatedEvent event) {
        this.service.createAccounts(event.organizationId());
    }
}
