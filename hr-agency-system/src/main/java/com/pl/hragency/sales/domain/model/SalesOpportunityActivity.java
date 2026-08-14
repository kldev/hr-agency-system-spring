package com.pl.hragency.sales.domain.model;

import java.time.Instant;
import java.util.UUID;

public final class SalesOpportunityActivity {

    private final SalesOpportunityActivityId id;
    private final UUID organizationId;
    private final SalesOpportunityId salesOpportunityId;

    private final SalesActivityType type;
    private final String note;

    private final Instant occurredAt;
    private final Instant createdAt;
    private final UUID createdBy;

    private SalesOpportunityActivity(
            SalesOpportunityActivityId id,
            UUID organizationId,
            SalesOpportunityId salesOpportunityId,
            SalesActivityType type,
            String note,
            Instant occurredAt,
            Instant createdAt,
            UUID createdBy) {

        this.id = id;
        this.organizationId = organizationId;
        this.salesOpportunityId = salesOpportunityId;
        this.type = type;
        this.note = note;
        this.occurredAt = occurredAt;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public static SalesOpportunityActivity create(
            UUID organizationId,
            SalesOpportunityId salesOpportunityId,
            SalesActivityType type,
            String note,
            Instant occurredAt,
            UUID createdBy) {

        Instant now = Instant.now();

        return new SalesOpportunityActivity(
                SalesOpportunityActivityId.newId(),
                organizationId,
                salesOpportunityId,
                type,
                note,
                occurredAt,
                now,
                createdBy
        );
    }

    public static SalesOpportunityActivity rehydrate(
            SalesOpportunityActivityId id,
            UUID organizationId,
            SalesOpportunityId salesOpportunityId,
            SalesActivityType type,
            String note,
            Instant occurredAt,
            Instant createdAt,
            UUID createdBy) {

        return new SalesOpportunityActivity(
                id,
                organizationId,
                salesOpportunityId,
                type,
                note,
                occurredAt,
                createdAt,
                createdBy
        );
    }

    public SalesOpportunityActivityId id() {
        return id;
    }

    public UUID organizationId() {
        return organizationId;
    }

    public SalesOpportunityId salesOpportunityId() {
        return salesOpportunityId;
    }

    public SalesActivityType type() {
        return type;
    }

    public String note() {
        return note;
    }

    public Instant occurredAt() {
        return occurredAt;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public UUID createdBy() {
        return createdBy;
    }
}