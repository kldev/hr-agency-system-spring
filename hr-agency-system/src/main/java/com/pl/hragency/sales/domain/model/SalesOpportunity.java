package com.pl.hragency.sales.domain.model;

import com.pl.hragency.sales.domain.event.SalesOpportunityStageChanged;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public final class SalesOpportunity {

    private final SalesOpportunityId id;
    private final UUID organizationId;
    private final UUID companyId;

    private String title;
    private String description;

    private SalesOpportunityStage stage;

    private BigDecimal expectedValue;
    private String currencyCode;

    private LocalDate expectedCloseDate;

    private String lostReason;

    private UUID salesOwnerId;

    private final Instant createdAt;

    private SalesOpportunity(
            SalesOpportunityId id,
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

    public static SalesOpportunity create(
            UUID organizationId,
            UUID companyId,
            String title,
            String description,
            BigDecimal expectedValue,
            String currencyCode,
            LocalDate expectedCloseDate,
            UUID salesOwnerId
    ) {
        if (organizationId == null) {
            throw new IllegalArgumentException("Organization id cannot be null");
        }

        if (companyId == null) {
            throw new IllegalArgumentException("Company id cannot be null");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }

        if (expectedValue != null && expectedValue.signum() < 0) {
            throw new IllegalArgumentException(
                    "Expected value cannot be negative"
            );
        }

        return new SalesOpportunity(
                SalesOpportunityId.newId(),
                organizationId,
                companyId,
                title,
                description,
                SalesOpportunityStage.NEW,
                expectedValue,
                currencyCode,
                expectedCloseDate,
                null,
                salesOwnerId,
                Instant.now()
        );
    }

    public static SalesOpportunity rehydrate(
            SalesOpportunityId id,
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
        return new SalesOpportunity(
                id,
                organizationId,
                companyId,
                title,
                description,
                stage,
                expectedValue,
                currencyCode,
                expectedCloseDate,
                lostReason,
                salesOwnerId,
                createdAt
        );
    }

    public SalesOpportunityStageChanged changeStage(
            SalesOpportunityStage newStage,
            String lostReason
    ) {
        if (newStage == null) {
            throw new IllegalArgumentException(
                    "Stage cannot be null"
            );
        }

        if (stage == newStage) {
            throw new IllegalStateException(
                    "Sales opportunity is already in stage " + newStage
            );
        }

        validateTransition(newStage);

        var previousStage = stage;

        if (newStage == SalesOpportunityStage.LOST) {
            if (lostReason == null || lostReason.isBlank()) {
                throw new IllegalArgumentException(
                        "Lost reason is required when opportunity is lost"
                );
            }

            this.lostReason = lostReason;
        } else {
            this.lostReason = null;
        }

        this.stage = newStage;

        return new SalesOpportunityStageChanged(
                organizationId,
                id.value(),
                companyId,
                previousStage,
                newStage,
                salesOwnerId,
                Instant.now()
        );
    }

    private void validateTransition(
            SalesOpportunityStage newStage
    ) {
        boolean allowed = switch (stage) {
            case NEW ->
                    newStage == SalesOpportunityStage.CONTACTED;

            case CONTACTED ->
                    newStage == SalesOpportunityStage.QUALIFIED;

            case QUALIFIED ->
                    newStage == SalesOpportunityStage.PROPOSAL;

            case PROPOSAL ->
                    newStage == SalesOpportunityStage.WON
                            || newStage == SalesOpportunityStage.LOST;

            case WON, LOST ->
                    false;
        };

        if (!allowed) {
            throw new IllegalStateException(
                    "Cannot change opportunity stage from "
                            + stage
                            + " to "
                            + newStage
            );
        }
    }

    public SalesOpportunityId id() {
        return id;
    }

    public UUID organizationId() {
        return organizationId;
    }

    public UUID companyId() {
        return companyId;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public SalesOpportunityStage stage() {
        return stage;
    }

    public BigDecimal expectedValue() {
        return expectedValue;
    }

    public String currencyCode() {
        return currencyCode;
    }

    public LocalDate expectedCloseDate() {
        return expectedCloseDate;
    }

    public String lostReason() {
        return lostReason;
    }

    public UUID salesOwnerId() {
        return salesOwnerId;
    }

    public Instant createdAt() {
        return createdAt;
    }
}