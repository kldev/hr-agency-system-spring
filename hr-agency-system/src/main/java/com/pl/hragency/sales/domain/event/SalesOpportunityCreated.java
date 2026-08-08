package com.pl.hragency.sales.domain.event;

import com.pl.hragency.shared.event.DomainEvent;
import com.pl.hragency.shared.event.UserSnapshot;

import java.util.UUID;

public record SalesOpportunityCreated (UUID opportunityId, UUID companyId, UserSnapshot owner)  implements DomainEvent {
}
