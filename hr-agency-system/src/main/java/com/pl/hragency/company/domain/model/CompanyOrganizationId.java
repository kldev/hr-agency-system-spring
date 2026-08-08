package com.pl.hragency.company.domain.model;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CompanyOrganizationId(@NotNull UUID value) {
    public CompanyOrganizationId {
        if (value == null)
            throw new IllegalArgumentException(
                "Organization id cannot be null");
    }
}
