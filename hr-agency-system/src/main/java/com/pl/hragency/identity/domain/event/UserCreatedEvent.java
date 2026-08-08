package com.pl.hragency.identity.domain.event;

import com.pl.hragency.shared.event.DomainEvent;

import java.util.UUID;

public record UserCreatedEvent(UUID userId, UUID organizationId, String fullName, String email) implements DomainEvent {
}
