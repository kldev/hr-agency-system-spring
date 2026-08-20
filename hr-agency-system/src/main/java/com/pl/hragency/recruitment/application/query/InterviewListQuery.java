package com.pl.hragency.recruitment.application.query;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public record InterviewListQuery(UUID createdBy,
                                 LocalDate from,
                                 LocalDate to,
                                 ZoneId timezone,
                                 String search) {

    public Instant fromAtInstant() {
        return from == null ? null : from.atStartOfDay(timezone).toInstant();
    }

    public Instant toAtInstant() {
        return to == null ? null : to.plusDays(1).atStartOfDay(timezone).toInstant();
    }

}
