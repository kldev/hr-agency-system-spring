package com.pl.hragency.sales.domain.event;

import com.pl.hragency.shared.event.DomainEvent;
import com.pl.hragency.shared.event.UserSnapshot;

import java.time.Instant;
import java.util.UUID;

public record SalesOpportunityActivityCreatedEvent(UUID activityId,
                                                   UUID opportunityId,
                                                   UUID organizationId,
                                                   UserSnapshot createdBy,
                                                   UUID actorId,
                                                   String actorName,
                                                   Instant occurredOn) implements DomainEvent {
}
