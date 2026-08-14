package com.pl.hragency.recruitment.domain.event;

import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record JobApplicationNoteCreatedEvent(UUID applicationId,
                                            UUID candidateId,
                                            UUID organizationId,
                                            String note,
                                            UUID actorId,
                                            String actorName,
                                            Instant occurredOn) implements DomainEvent {
}
