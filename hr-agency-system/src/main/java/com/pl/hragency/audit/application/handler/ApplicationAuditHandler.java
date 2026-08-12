package com.pl.hragency.audit.application.handler;

import com.pl.hragency.audit.application.AuditService;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.recruitment.domain.event.ApplicationCreatedEvent;
import com.pl.hragency.recruitment.domain.event.ApplicationStatusChangedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class ApplicationAuditHandler {
    private final AuditService auditService;

    public ApplicationAuditHandler(AuditService auditService) {
        this.auditService = auditService;
    }

    @ApplicationModuleListener
    void on(ApplicationCreatedEvent event) {
        auditService.record(
                "recruitment",
                "Application",
                event.applicationId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.CREATED,
                event
        );
    }

    @ApplicationModuleListener
    void on(ApplicationStatusChangedEvent event) {
        auditService.record(
                "recruitment",
                "Application",
                event.applicationId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.STATUS_CHANGED,
                event
        );
    }
}
