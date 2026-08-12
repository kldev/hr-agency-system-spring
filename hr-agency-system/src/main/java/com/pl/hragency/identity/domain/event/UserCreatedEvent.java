package com.pl.hragency.identity.domain.event;

import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record UserCreatedEvent(UUID userId, UUID organizationId, String fullName, String email,
                               UUID actorId, String actorName, Instant occurredOn) implements DomainEvent {
}
