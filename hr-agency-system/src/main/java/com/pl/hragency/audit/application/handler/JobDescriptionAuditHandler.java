package com.pl.hragency.audit.application.handler;

import com.pl.hragency.audit.application.AuditService;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.jobdescription.domain.event.JobDescriptionCreatedEvent;

import com.pl.hragency.jobdescription.domain.event.JobDescriptionStatusUpdatedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class JobDescriptionAuditHandler {
    private final AuditService auditService;

    public JobDescriptionAuditHandler(AuditService auditService) {
        this.auditService = auditService;
    }

    @ApplicationModuleListener
    void on(JobDescriptionCreatedEvent event) {
        auditService.record(
                "job-description",
                "JobDescription",
                event.jobDescriptionId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.CREATED,
                event
        );
    }

    @ApplicationModuleListener
    void on(JobDescriptionStatusUpdatedEvent event) {
        auditService.record(
                "job-description",
                "JobDescription",
                event.jobDescriptionId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.STATUS_CHANGED,
                event
        );
    }
}
