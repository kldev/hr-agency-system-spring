package com.pl.hragency.sales.domain.event;

import com.pl.hragency.shared.event.DomainEvent;
import com.pl.hragency.shared.event.UserSnapshot;

import java.time.Instant;
import java.util.UUID;

public record SalesOpportunityCreatedEvent(UUID opportunityId,
                                           UUID companyId,
                                           UUID organizationId,
                                           UserSnapshot owner,
                                           UUID actorId,
                                           String actorName,
                                           Instant occurredOn)  implements DomainEvent {
}
