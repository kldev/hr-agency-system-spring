package com.pl.hragency.recruitment.domain.event;

import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record InterviewScheduledEvent(UUID interviewId,
                                      UUID organizationId,
                                      UUID candidateId,
                                      UUID applicationId,
                                      UUID actorId,
                                      String actorName,
                                      Instant occurredOn
                                      ) implements DomainEvent {
}
