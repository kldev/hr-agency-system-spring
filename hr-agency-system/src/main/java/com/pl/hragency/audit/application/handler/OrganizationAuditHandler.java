package com.pl.hragency.audit.application.handler;

import com.pl.hragency.audit.application.AuditService;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.organization.domain.event.OrganizationCreatedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class OrganizationAuditHandler {

    private final AuditService auditService;

    public OrganizationAuditHandler(AuditService auditService) {
        this.auditService = auditService;
    }

    @ApplicationModuleListener
    void on(OrganizationCreatedEvent event) {
        auditService.record(
                "organization",
                "Organization",
                event.organizationId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.CREATED,
                event
        );
    }
}
