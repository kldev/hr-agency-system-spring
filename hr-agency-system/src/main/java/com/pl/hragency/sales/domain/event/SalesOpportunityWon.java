package com.pl.hragency.sales.domain.event;

import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record SalesOpportunityWon(

        UUID organizationId,

        UUID salesOpportunityId,

        UUID companyId,

        UUID salesOwnerId,

        Instant occurredAt
)  implements DomainEvent {
}