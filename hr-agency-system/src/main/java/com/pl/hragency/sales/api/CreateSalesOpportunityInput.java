package com.pl.hragency.sales.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateSalesOpportunityInput(

        UUID companyId,

        String title,

        String description,

        BigDecimal expectedValue,

        String currencyCode,

        LocalDate expectedCloseDate,

        UUID salesOwnerId
) {
}
