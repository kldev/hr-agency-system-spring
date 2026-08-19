package com.pl.hragency.sales.application.query;

import com.pl.hragency.sales.domain.model.SalesActivityType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

public record SalesOpportunityActivityQuery(
        UUID salesOpportunityId,
        SalesActivityType type,
        LocalDate occurredFrom,
        LocalDate occurredTo,
        String search,
        ZoneId timezone
) {
    public Instant occurredFromAtInstant() {
        return occurredFrom == null ? null : occurredFrom.atStartOfDay(timezone).toInstant();
    }

    public Instant occurredToAtInstant() {
        return occurredTo == null ? null : occurredTo.plusDays(1).atStartOfDay(timezone).toInstant();
    }

}
