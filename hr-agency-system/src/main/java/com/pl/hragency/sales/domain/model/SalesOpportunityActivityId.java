package com.pl.hragency.sales.domain.model;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;


public record SalesOpportunityActivityId(@NotNull UUID value) {
    public SalesOpportunityActivityId {
        if (value == null)
            throw new IllegalArgumentException("Sales Activity id cannot be null");
    }

    public static SalesOpportunityActivityId newId(){
        return new SalesOpportunityActivityId(UUID.randomUUID());
    }
}
