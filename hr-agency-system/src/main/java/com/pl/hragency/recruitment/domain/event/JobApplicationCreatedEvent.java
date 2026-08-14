package com.pl.hragency.recruitment.domain.event;


import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record JobApplicationCreatedEvent(UUID applicationId,
                                         UUID jobPostingId,
                                         String jobTitle,
                                         UUID organizationId,
                                         UUID candidateId,
                                         String email,
                                         CandidateSource source,
                                         UUID actorId,
                                         String actorName,
                                         Instant occurredOn) implements DomainEvent {
}
