package com.pl.hragency.recruitment.domain.event;

import com.pl.hragency.recruitment.domain.model.candidate.CandidateStatus;
import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CandidateStatusChangedEvent(UUID candidateId, UUID organizationId,
                                          CandidateStatus oldStatus,
                                          CandidateStatus newStatus,
                                          UUID actorId,
                                          String actorName,
                                          Instant occurredOn) implements DomainEvent {
}
