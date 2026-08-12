package com.pl.hragency.recruitment.domain.event;

import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CandidateUpdatedEvent(UUID candidateId,
                                    UUID organizationId,
                                    String firstName,
                                    String lastName,
                                    String email,
                                    UUID actorId,
                                    String actorName,
                                    Instant occurredOn) implements DomainEvent {
}
