package com.pl.hragency.recruitment.domain.event;


import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ApplicationCreatedEvent(UUID applicationId,
                                      UUID organizationId,
                                      UUID candidateId,
                                      String email,
                                      String source,
                                      UUID actorId,
                                      String actorName,
                                      Instant occurredOn) implements DomainEvent {
}
