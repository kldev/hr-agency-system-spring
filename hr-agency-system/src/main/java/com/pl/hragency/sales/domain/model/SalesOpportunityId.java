package com.pl.hragency.sales.domain.model;

import java.util.UUID;

public record SalesOpportunityId(UUID value) {

    public SalesOpportunityId {
        if (value == null) {
            throw new IllegalArgumentException("Sales opportunity id cannot be null");
        }
    }

    public static SalesOpportunityId newId() {
        return new SalesOpportunityId(UUID.randomUUID());
    }
}
