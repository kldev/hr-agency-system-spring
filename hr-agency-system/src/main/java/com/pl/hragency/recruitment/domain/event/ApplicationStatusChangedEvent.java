package com.pl.hragency.recruitment.domain.event;

import com.pl.hragency.recruitment.domain.model.application.ApplicationStatus;
import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ApplicationStatusChangedEvent(UUID applicationId,
                                            UUID candidateId,
                                            UUID organizationId,
                                            ApplicationStatus oldStatus,
                                            ApplicationStatus newStatus,
                                            UUID actorId,
                                            String actorName,
                                            Instant occurredOn)
        implements DomainEvent {
}
