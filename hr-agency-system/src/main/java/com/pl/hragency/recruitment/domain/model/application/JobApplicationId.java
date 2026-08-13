package com.pl.hragency.recruitment.domain.model.application;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record JobApplicationId(@NotNull UUID value) {

    public static JobApplicationId newId() {
        return new JobApplicationId(UUID.randomUUID());
    }
}
