package com.pl.hragency.sales.application.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateSalesOpportunityCommand(

        UUID companyId,

        String title,

        String description,

        BigDecimal expectedValue,

        String currencyCode,

        LocalDate expectedCloseDate,

        UUID salesOwnerId
) {
}