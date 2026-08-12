package com.pl.hragency.audit.application.handler;

import com.pl.hragency.audit.application.AuditService;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.recruitment.domain.event.CandidateCreatedEvent;
import com.pl.hragency.recruitment.domain.event.CandidateStatusChangedEvent;
import com.pl.hragency.recruitment.domain.event.CandidateUpdatedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class CandidateAuditHandler {

    private final AuditService auditService;

    public CandidateAuditHandler(AuditService auditService) {
        this.auditService = auditService;
    }

    @ApplicationModuleListener
    public void handle(CandidateCreatedEvent event) {
        auditService.record("recruitment",
                "Candidate",
                event.candidateId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.CREATED,
                event
        );
    }

    @ApplicationModuleListener
    public void handle(CandidateUpdatedEvent event) {
        auditService.record("recruitment",
                "Candidate",
                event.candidateId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.UPDATED,
                event
        );
    }

    @ApplicationModuleListener
    public void handle(CandidateStatusChangedEvent event) {
        auditService.record("recruitment",
                "Candidate",
                event.candidateId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.STATUS_CHANGED,
                event
        );
    }
}