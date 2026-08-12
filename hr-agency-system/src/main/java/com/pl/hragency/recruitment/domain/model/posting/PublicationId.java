package com.pl.hragency.recruitment.domain.model.posting;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PublicationId(@NotNull UUID value) {

    public static PublicationId newId() { return new PublicationId(UUID.randomUUID());}
}
