package com.pl.hragency.audit.application.handler;

import com.pl.hragency.audit.application.AuditService;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.company.domain.event.CompanyCreatedEvent;
import com.pl.hragency.company.domain.event.CompanySalesOwnerChangedEvent;
import com.pl.hragency.organization.domain.event.OrganizationCreatedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class CompanyAuditHandler {

    private final AuditService auditService;

    public CompanyAuditHandler(AuditService auditService) {
        this.auditService = auditService;
    }

    @ApplicationModuleListener
    void on(CompanyCreatedEvent event) {
        auditService.record(
                "company",
                "Company",
                event.companyId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.CREATED,
                event
        );
    }

    @ApplicationModuleListener
    void on(CompanySalesOwnerChangedEvent event) {
        auditService.record(
                "company",
                "Company",
                event.companyId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.SALES_OWNER_CHANGED,
                event
        );
    }
}
