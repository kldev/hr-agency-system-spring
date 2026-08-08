package com.pl.hragency.audit.application.handler;

import com.pl.hragency.audit.application.AuditService;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.sales.domain.event.SalesOpportunityCreated;
import com.pl.hragency.sales.domain.event.SalesOpportunityLost;
import com.pl.hragency.sales.domain.event.SalesOpportunityStageChanged;
import com.pl.hragency.sales.domain.event.SalesOpportunityWon;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;


@Service
public class SalesAuditHandler {

    private final AuditService auditService;

    public SalesAuditHandler(AuditService auditService) {
        this.auditService = auditService;
    }

    @ApplicationModuleListener
    void on(SalesOpportunityCreated event) {
        auditService.record(
                "sales",
                "SalesOpportunity",
                event.opportunityId(),
                AuditEventType.CREATED,
                event
        );
    }

    @ApplicationModuleListener
    void on(SalesOpportunityStageChanged event) {
        auditService.record(
                "sales",
                "SalesOpportunity",
                event.salesOpportunityId(),
                AuditEventType.STATUS_CHANGED,
                event
        );
    }

    @ApplicationModuleListener
    void on(SalesOpportunityWon event) {
        auditService.record(
                "sales",
                "SalesOpportunity",
                event.salesOpportunityId(),
                AuditEventType.STATUS_CHANGED,
                event
        );
    }

    @ApplicationModuleListener
    void on(SalesOpportunityLost event) {
        auditService.record(
                "sales",
                "SalesOpportunity",
                event.salesOpportunityId(),
                AuditEventType.STATUS_CHANGED,
                event
        );
    }
}
