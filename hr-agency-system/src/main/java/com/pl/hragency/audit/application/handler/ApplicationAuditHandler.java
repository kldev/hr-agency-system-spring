package com.pl.hragency.audit.application.handler;

import com.pl.hragency.audit.application.AuditService;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.recruitment.domain.event.JobApplicationCreatedEvent;
import com.pl.hragency.recruitment.domain.event.JobApplicationStatusChangedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class ApplicationAuditHandler {
    private final AuditService auditService;

    public ApplicationAuditHandler(AuditService auditService) {
        this.auditService = auditService;
    }

    @ApplicationModuleListener
    void on(JobApplicationCreatedEvent event) {
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
    void on(JobApplicationStatusChangedEvent event) {
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
