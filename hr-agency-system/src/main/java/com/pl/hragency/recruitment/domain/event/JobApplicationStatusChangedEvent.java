package com.pl.hragency.recruitment.domain.event;

import com.pl.hragency.recruitment.domain.model.application.JobApplicationStatus;
import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record JobApplicationStatusChangedEvent(UUID applicationId,
                                               UUID candidateId,
                                               UUID organizationId,
                                               JobApplicationStatus oldStatus,
                                               JobApplicationStatus newStatus,
                                               UUID actorId,
                                               String actorName,
                                               Instant occurredOn)
        implements DomainEvent {
}
