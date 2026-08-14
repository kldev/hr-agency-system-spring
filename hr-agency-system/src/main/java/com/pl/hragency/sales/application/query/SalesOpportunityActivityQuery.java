package com.pl.hragency.sales.application.query;

import com.pl.hragency.sales.domain.model.SalesActivityType;

import java.time.LocalDate;
import java.util.UUID;

public record SalesOpportunityActivityQuery(
        UUID salesOpportunityId,
        SalesActivityType type,
        LocalDate occurredFrom,
        LocalDate occurredTo,
        String search
) {
}
