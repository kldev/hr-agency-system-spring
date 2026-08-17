package com.pl.hragency.organization.domain.event;

import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record OrganizationCreateAdminEvent(UUID organizationId,
                                           String email,
                                           String firstName,
                                           String lastName,
                                           String password,
                                           UUID actorId,
                                           String actorName,
                                           Instant occurredOn) implements DomainEvent {
}
