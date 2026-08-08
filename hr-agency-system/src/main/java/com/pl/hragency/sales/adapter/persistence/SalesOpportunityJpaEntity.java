package com.pl.hragency.sales.adapter.persistence;

import com.pl.hragency.sales.domain.model.SalesOpportunityStage;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "sales_opportunities"
)
public class SalesOpportunityJpaEntity {

    @Id
    private UUID id;

    @Column(
            name = "organization_id",
            nullable = false
    )
    private UUID organizationId;

    @Column(
            name = "company_id",
            nullable = false
    )
    private UUID companyId;

    @Column(
            nullable = false,
            length = 255
    )
    private String title;

    @Column(
            length = 2000
    )
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private SalesOpportunityStage stage;

    @Column(
            name = "expected_value",
            precision = 15,
            scale = 2
    )
    private BigDecimal expectedValue;

    @Column(
            name = "currency_code",
            length = 3
    )
    private String currencyCode;

    @Column(
            name = "expected_close_date"
    )
    private LocalDate expectedCloseDate;

    @Column(
            name = "lost_reason",
            length = 1000
    )
    private String lostReason;

    @Column(
            name = "sales_owner_id"
    )
    private UUID salesOwnerId;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;

    protected SalesOpportunityJpaEntity() {
    }

    public SalesOpportunityJpaEntity(
            UUID id,
            UUID organizationId,
            UUID companyId,
            String title,
            String description,
            SalesOpportunityStage stage,
            BigDecimal expectedValue,
            String currencyCode,
            LocalDate expectedCloseDate,
            String lostReason,
            UUID salesOwnerId,
            Instant createdAt
    ) {
        this.id = id;
        this.organizationId = organizationId;
        this.companyId = companyId;
        this.title = title;
        this.description = description;
        this.stage = stage;
        this.expectedValue = expectedValue;
        this.currencyCode = currencyCode;
        this.expectedCloseDate = expectedCloseDate;
        this.lostReason = lostReason;
        this.salesOwnerId = salesOwnerId;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public SalesOpportunityStage getStage() {
        return stage;
    }

    public BigDecimal getExpectedValue() {
        return expectedValue;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public LocalDate getExpectedCloseDate() {
        return expectedCloseDate;
    }

    public String getLostReason() {
        return lostReason;
    }

    public UUID getSalesOwnerId() {
        return salesOwnerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}