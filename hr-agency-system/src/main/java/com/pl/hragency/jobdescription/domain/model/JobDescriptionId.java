package com.pl.hragency.jobdescription.domain.model;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record JobDescriptionId(@NotNull UUID value){
    public JobDescriptionId {
        if (value == null) {
            throw new IllegalArgumentException("Company contact id cannot be null");
        }
    }

    public static JobDescriptionId newId() {
        return new JobDescriptionId(UUID.randomUUID());
    }
}
