package com.pl.hragency.recruitment.domain.event;

import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record JobPostingStatusUpdatedEvent(UUID jobPostingId,
                                           UUID organizationId,
                                           JobPostingStatus oldStatus,
                                           JobPostingStatus newStatus,
                                           UUID actorId,
                                           String actorName,
                                           Instant occurredOn) implements DomainEvent {
}
