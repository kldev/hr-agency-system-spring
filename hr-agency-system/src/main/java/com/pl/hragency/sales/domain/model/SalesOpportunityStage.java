package com.pl.hragency.sales.domain.model;


import java.util.Arrays;

public enum SalesOpportunityStage {
    NEW,
    CONTACTED,
    QUALIFIED,
    PROPOSAL,
    WON,
    LOST;

    public static SalesOpportunityStage from(String value) {
        return Arrays.stream(values())
                .filter(r -> r.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unknown sales opportunity stage: " + value
                        ));
    }
}
