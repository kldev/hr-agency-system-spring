package com.pl.hragency.audit.application.handler;

import com.pl.hragency.audit.application.AuditService;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.recruitment.domain.event.JobPostingCreatedEvent;
import com.pl.hragency.recruitment.domain.event.JobPostingRecruiterUpdatedEvent;
import com.pl.hragency.recruitment.domain.event.JobPostingStatusUpdatedEvent;
import com.pl.hragency.recruitment.domain.event.JobPostingUpdatedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class JobPostingAuditHandler {
    private final AuditService auditService;

    public JobPostingAuditHandler(AuditService auditService) {
        this.auditService = auditService;
    }

    @ApplicationModuleListener
    void on(JobPostingCreatedEvent event) {
        auditService.record(
                "recruitment",
                "JobPosting",
                event.jobPostingId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.CREATED,
                event
        );
    }

    @ApplicationModuleListener
    void on(JobPostingStatusUpdatedEvent event) {
        auditService.record(
                "recruitment",
                "JobPosting",
                event.jobPostingId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.STATUS_CHANGED,
                event
        );
    }

    @ApplicationModuleListener
    void on(JobPostingUpdatedEvent event) {
        auditService.record(
                "recruitment",
                "JobPosting",
                event.jobPostingId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.UPDATED,
                event
        );
    }

    @ApplicationModuleListener
    void on(JobPostingRecruiterUpdatedEvent event) {
        auditService.record(
                "recruitment",
                "JobPosting",
                event.jobPostingId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.UPDATED,
                event
        );
    }
}
