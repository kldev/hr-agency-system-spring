package com.pl.hragency.recruitment.domain.model.posting;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record JobPostingId(@NotNull UUID value){
    public JobPostingId {
        if (value == null) {
            throw new IllegalArgumentException("Company contact id cannot be null");
        }
    }

    public static JobPostingId newId() {
        return new JobPostingId(UUID.randomUUID());
    }
}