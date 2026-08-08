package com.pl.hragency.sales.domain.event;

import com.pl.hragency.sales.domain.model.SalesOpportunityStage;
import com.pl.hragency.shared.event.DomainEvent;
import com.pl.hragency.shared.event.UserSnapshot;

import java.time.Instant;
import java.util.UUID;

public record SalesOpportunityStageChanged(

        UUID organizationId,

        UUID salesOpportunityId,

        UUID companyId,

        SalesOpportunityStage previousStage,

        SalesOpportunityStage newStage,

        UUID salesOwnerId,

        Instant occurredAt
)  implements DomainEvent {
}
