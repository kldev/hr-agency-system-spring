package com.pl.hragency.recruitment.domain.event;

import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CandidatePhoneChangedEvent(UUID candidateId, UUID organizationId,
                                         String oldPhone, String newPhone,
                                         UUID actorId,
                                         String actorName,
                                         Instant occurredOn) implements DomainEvent {
}
