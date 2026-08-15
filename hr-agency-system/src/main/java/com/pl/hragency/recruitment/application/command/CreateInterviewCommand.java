package com.pl.hragency.recruitment.application.command;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public record CreateInterviewCommand(LocalDateTime scheduledAt, ZoneId scheduledTimezone) {
    public Instant scheduledAtInstant() {
        return scheduledAt
                .atZone(scheduledTimezone)
                .toInstant();
    }
}
