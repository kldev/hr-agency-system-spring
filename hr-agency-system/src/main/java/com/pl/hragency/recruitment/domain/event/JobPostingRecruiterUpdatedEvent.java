package com.pl.hragency.recruitment.domain.event;


import com.pl.hragency.shared.event.DomainEvent;
import com.pl.hragency.shared.event.UserSnapshot;

import java.time.Instant;
import java.util.UUID;

public record JobPostingRecruiterUpdatedEvent(UUID jobPostingId,
                                             UUID organizationId,
                                             UserSnapshot oldRecruiter,
                                             UserSnapshot newRecruiter,
                                              UUID actorId,
                                              String actorName,
                                              Instant occurredOn) implements DomainEvent {

}
