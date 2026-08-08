package com.pl.hragency.sales.domain.event;

import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record SalesOpportunityLost(UUID organizationId,
                                   UUID salesOpportunityId,
                                   UUID companyId,
                                   UUID salesOwnerId,
                                   String lostReason,
                                   Instant occurredAt)  implements DomainEvent {
}
