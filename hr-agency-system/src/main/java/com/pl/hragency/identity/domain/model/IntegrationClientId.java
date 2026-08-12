package com.pl.hragency.identity.domain.model;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record IntegrationClientId(@NotNull UUID value) {
    public static IntegrationClientId newId(){
        return new IntegrationClientId(UUID.randomUUID());
    }
}
