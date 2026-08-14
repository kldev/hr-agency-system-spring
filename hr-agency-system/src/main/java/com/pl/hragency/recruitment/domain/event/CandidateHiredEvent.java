package com.pl.hragency.recruitment.domain.event;

import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CandidateHiredEvent( UUID candidateId,
                                   UUID jobApplicationId,
                                   String jobTitle,
                                   UUID jobId,
                                   UUID organizationId,
                                   UUID actorId,
                                   String actorName,
                                   Instant occurredOn) implements DomainEvent {
}
