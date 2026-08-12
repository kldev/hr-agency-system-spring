package com.pl.hragency.audit.application.handler;

import com.pl.hragency.audit.application.AuditService;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.sales.domain.event.SalesOpportunityCreatedEvent;
import com.pl.hragency.sales.domain.event.SalesOpportunityLostEvent;
import com.pl.hragency.sales.domain.event.SalesOpportunityStageChangedEvent;
import com.pl.hragency.sales.domain.event.SalesOpportunityWonEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;


@Service
public class SalesAuditHandler {

    private final AuditService auditService;

    public SalesAuditHandler(AuditService auditService) {
        this.auditService = auditService;
    }

    @ApplicationModuleListener
    void on(SalesOpportunityCreatedEvent event) {
        auditService.record(
                "sales",
                "SalesOpportunity",
                event.opportunityId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.CREATED,
                event
        );
    }

    @ApplicationModuleListener
    void on(SalesOpportunityStageChangedEvent event) {
        auditService.record(
                "sales",
                "SalesOpportunity",
                event.salesOpportunityId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.STATUS_CHANGED,
                event
        );
    }

    @ApplicationModuleListener
    void on(SalesOpportunityWonEvent event) {
        auditService.record(
                "sales",
                "SalesOpportunity",
                event.salesOpportunityId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.STATUS_CHANGED,
                event
        );
    }

    @ApplicationModuleListener
    void on(SalesOpportunityLostEvent event) {
        auditService.record(
                "sales",
                "SalesOpportunity",
                event.salesOpportunityId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.STATUS_CHANGED,
                event
        );
    }
}
