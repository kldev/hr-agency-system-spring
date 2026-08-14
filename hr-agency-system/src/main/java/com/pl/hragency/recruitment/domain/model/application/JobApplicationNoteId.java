package com.pl.hragency.recruitment.domain.model.application;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record JobApplicationNoteId(@NotNull UUID value) {
    public JobApplicationNoteId {
        if (value == null) throw new IllegalArgumentException("JobApplicationNoteId cannot be null");
    }

    public static JobApplicationNoteId newId() { return new JobApplicationNoteId(UUID.randomUUID()); }
}
