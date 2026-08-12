package com.pl.hragency.recruitment.domain.event;

import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CandidateEmailChangedEvent(UUID candidateId, UUID organizationId,
                                         String oldEmail, String newEmail,
                                         UUID actorId,
                                         String actorName,
                                         Instant occurredOn) implements DomainEvent {
}
