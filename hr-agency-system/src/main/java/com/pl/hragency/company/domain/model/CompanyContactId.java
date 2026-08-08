package com.pl.hragency.company.domain.model;


import java.util.UUID;

public record CompanyContactId(UUID value) {

    public CompanyContactId {
        if (value == null) {
            throw new IllegalArgumentException("Company contact id cannot be null");
        }
    }

    public static CompanyContactId newId() {
        return new CompanyContactId(UUID.randomUUID());
    }
}
