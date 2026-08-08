package com.pl.hragency.company.domain.model;

public record RegistrationNumber(String value) {

    public RegistrationNumber {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Registration number cannot be empty"
            );
        }
    }
}
