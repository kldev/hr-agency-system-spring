package com.pl.hragency.jobdescription.domain.event;



import com.pl.hragency.jobdescription.domain.model.JobDescriptionStatus;
import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record JobDescriptionStatusUpdatedEvent(UUID jobDescriptionId,  UUID organizationId,
                                               JobDescriptionStatus oldStatus,
                                               JobDescriptionStatus newStatus,
                                               UUID actorId,
                                               String actorName,
                                               Instant occurredOn) implements DomainEvent {
}
