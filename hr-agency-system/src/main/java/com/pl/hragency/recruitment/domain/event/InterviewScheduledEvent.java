package com.pl.hragency.recruitment.domain.event;

import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record InterviewScheduledEvent(UUID interviewId,
                                      UUID organizationId,
                                      UUID candidateId,
                                      UUID applicationId,
                                      UUID actorId,
                                      Instant interviewDate,
                                      String actorName,
                                      Instant occurredOn
                                      ) implements DomainEvent {
}
