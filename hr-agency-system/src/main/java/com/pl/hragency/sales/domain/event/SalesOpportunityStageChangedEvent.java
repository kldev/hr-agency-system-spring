package com.pl.hragency.sales.domain.event;

import com.pl.hragency.sales.domain.model.SalesOpportunityStage;
import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record SalesOpportunityStageChangedEvent(
        UUID organizationId,
        UUID salesOpportunityId,
        UUID companyId,
        SalesOpportunityStage previousStage,
        SalesOpportunityStage newStage,
        UUID salesOwnerId,
        UUID actorId,
        String actorName,
        Instant occurredOn
)  implements DomainEvent {
}
