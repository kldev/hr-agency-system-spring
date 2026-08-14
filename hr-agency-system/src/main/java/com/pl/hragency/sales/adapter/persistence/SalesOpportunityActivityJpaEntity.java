package com.pl.hragency.sales.adapter.persistence;

import com.pl.hragency.sales.domain.model.SalesActivityType;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "sales_opportunity_activities",
        indexes = {
                @Index(
                        name = "idx_sales_opportunity_activities_organization",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_sales_opportunity_activities_organization_opportunity",
                        columnList = "organization_id, sales_opportunity_id"
                ),
                @Index(
                        name = "idx_sales_opportunity_activities_opportunity_occurred",
                        columnList = "sales_opportunity_id, occurred_at"
                ),
                @Index(
                        name = "idx_sales_opportunity_activities_organization_type",
                        columnList = "organization_id, type"
                )
        }
)
public class SalesOpportunityActivityJpaEntity {

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

    protected SalesOpportunityActivityJpaEntity() {
    }

    public SalesOpportunityActivityJpaEntity(
            UUID id,
            UUID organizationId,
            UUID salesOpportunityId,
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

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
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

}