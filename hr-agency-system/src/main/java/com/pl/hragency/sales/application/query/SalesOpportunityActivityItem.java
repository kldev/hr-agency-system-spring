package com.pl.hragency.sales.application.query;

import com.pl.hragency.sales.domain.model.SalesActivityType;


import java.time.Instant;
import java.util.UUID;

public record SalesOpportunityActivityItem(
        UUID id,
        UUID salesOpportunityId,
        SalesActivityType type,
        String note,
        Instant occurredAt,
        Instant createdAt,
        UUID createdBy,
        String createdFullName
){}