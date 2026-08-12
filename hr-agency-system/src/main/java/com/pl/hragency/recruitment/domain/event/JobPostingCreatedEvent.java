package com.pl.hragency.recruitment.domain.event;

import com.pl.hragency.shared.event.DomainEvent;
import com.pl.hragency.shared.event.UserSnapshot;

import java.time.Instant;
import java.util.UUID;

public record JobPostingCreatedEvent( UUID jobPostingId,
                                      UUID organizationId,
                                      String title,
                                      UserSnapshot recruiter,
                                      UUID actorId,
                                      String actorName,
                                      Instant occurredOn) implements DomainEvent {
}
