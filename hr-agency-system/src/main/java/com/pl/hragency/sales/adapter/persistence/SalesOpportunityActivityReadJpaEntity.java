package com.pl.hragency.sales.adapter.persistence;

import com.pl.hragency.sales.domain.model.SalesActivityType;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sales_opportunity_activities_view")
public class SalesOpportunityActivityReadJpaEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "sales_opportunity_id", nullable = false)
    private UUID salesOpportunityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private SalesActivityType type;

    @Column(length = 500)
    private String note;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_by_full_name")
    private String createdFullName;

    protected SalesOpportunityActivityReadJpaEntity() {
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSalesOpportunityId() {
        return salesOpportunityId;
    }

    public SalesActivityType getType() {
        return type;
    }

    public String getNote() {
        return note;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public String getCreatedFullName() {
        return createdFullName;
    }
}
