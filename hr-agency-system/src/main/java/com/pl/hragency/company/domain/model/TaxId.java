package com.pl.hragency.company.domain.model;

public record TaxId(String value) {

    public TaxId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Tax ID cannot be empty");
        }
    }
}
