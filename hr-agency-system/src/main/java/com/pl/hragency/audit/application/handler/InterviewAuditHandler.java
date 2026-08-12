package com.pl.hragency.audit.application.handler;

import com.pl.hragency.audit.application.AuditService;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.recruitment.domain.event.InterviewScheduledEvent;
import com.pl.hragency.recruitment.domain.event.InterviewStatusChangedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class InterviewAuditHandler {
    private final AuditService auditService;

    public InterviewAuditHandler(AuditService auditService) {
        this.auditService = auditService;
    }

    @ApplicationModuleListener
    void on(InterviewScheduledEvent event) {
        auditService.record(
                "recruitment",
                "Interview",
                event.interviewId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.CREATED,
                event
        );
    }

    @ApplicationModuleListener
    void on(InterviewStatusChangedEvent event) {
        auditService.record(
                "recruitment",
                "Interview",
                event.interviewId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.STATUS_CHANGED,
                event
        );
    }
}
