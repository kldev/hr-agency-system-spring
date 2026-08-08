package com.pl.hragency.company.domain.model;


import java.util.UUID;

public record CompanyId(UUID value) {

    public CompanyId {
        if (value == null) {
            throw new IllegalArgumentException("Company id cannot be null");
        }
    }

    public static CompanyId newId() {
        return new CompanyId(UUID.randomUUID());
    }
}
