package com.pl.hragency.jobdescription.domain.event;

import com.pl.hragency.shared.event.DomainEvent;
import com.pl.hragency.shared.event.UserSnapshot;

import java.time.Instant;
import java.util.UUID;

public record JobDescriptionCreatedEvent(
        UUID jobDescriptionId,
        UUID organizationId,
        UUID companyId,
        String title,
        UserSnapshot user,
        UUID actorId,
        String actorName,
        Instant occurredOn) implements DomainEvent {
}
