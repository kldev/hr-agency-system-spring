package com.pl.hragency.audit.application.handler;

import com.pl.hragency.audit.application.AuditService;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.identity.domain.event.UserCreatedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class UserAuditHandler {
    private final AuditService auditService;

    public UserAuditHandler(AuditService auditService) {
        this.auditService = auditService;
    }


    @ApplicationModuleListener
    public void on(UserCreatedEvent event) {
        auditService.record(
                "identity",
                "User",
                event.userId(),
                event.actorId(),
                event.actorName(),
                AuditEventType.CREATED,
                event
        );
    }
}
