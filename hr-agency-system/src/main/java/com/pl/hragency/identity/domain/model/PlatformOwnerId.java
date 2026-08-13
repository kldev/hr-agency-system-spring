package com.pl.hragency.identity.domain.model;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PlatformOwnerId(@NotNull UUID value) {
    public static PlatformOwnerId newId(){
        return new PlatformOwnerId(UUID.randomUUID());
    }
}
