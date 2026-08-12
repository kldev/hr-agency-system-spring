package com.pl.hragency.sales.domain.event;

import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record SalesOpportunityLostEvent(UUID organizationId,
                                        UUID salesOpportunityId,
                                        UUID companyId,
                                        UUID salesOwnerId,
                                        String lostReason,
                                        UUID actorId,
                                        String actorName,
                                        Instant occurredOn)  implements DomainEvent {
}
