package com.pl.hragency.organization.domain.model;


import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record OrganizationId(@NotNull UUID value) {
    public static OrganizationId newId() {
        return new OrganizationId(UUID.randomUUID());
    }
}
