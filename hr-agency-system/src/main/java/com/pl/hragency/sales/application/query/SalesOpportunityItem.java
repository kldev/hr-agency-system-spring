package com.pl.hragency.sales.application.query;

import com.pl.hragency.sales.domain.model.SalesOpportunity;
import com.pl.hragency.sales.domain.model.SalesOpportunityStage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

public record SalesOpportunityItem(

        UUID id,

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

    public static SalesOpportunityItem from(
            SalesOpportunity opportunity
    ) {
        return new SalesOpportunityItem(
                opportunity.id().value(),
                opportunity.companyId(),
                opportunity.title(),
                opportunity.description(),
                opportunity.stage(),
                opportunity.expectedValue(),
                opportunity.currencyCode(),
                opportunity.expectedCloseDate(),
                opportunity.lostReason(),
                opportunity.salesOwnerId(),
                opportunity.createdAt()
        );
    }
}