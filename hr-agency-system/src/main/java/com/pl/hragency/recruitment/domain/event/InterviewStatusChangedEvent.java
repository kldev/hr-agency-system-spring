package com.pl.hragency.recruitment.domain.event;

import com.pl.hragency.recruitment.domain.model.interview.InterviewStatus;
import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record InterviewStatusChangedEvent(UUID interviewId,
                                          UUID organizationId,
                                          UUID candidateId,
                                          UUID applicationId,
                                          InterviewStatus oldStatus,
                                          InterviewStatus newStatus,
                                          UUID actorId,
                                          String actorName,
                                          Instant occurredOn) implements DomainEvent {
}
