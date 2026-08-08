package com.pl.hragency.identity.domain.model;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserId(@NotNull UUID value) {
    public static UserId newId(){
        return new UserId(UUID.randomUUID());
    }
}
