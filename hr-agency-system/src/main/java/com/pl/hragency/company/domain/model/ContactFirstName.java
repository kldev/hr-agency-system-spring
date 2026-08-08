package com.pl.hragency.company.domain.model;

public record ContactFirstName(String value) {

    public ContactFirstName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "First name cannot be empty"
            );
        }

        value = value.trim();

        if (value.length() > 100) {
            throw new IllegalArgumentException(
                    "First name cannot exceed 100 characters"
            );
        }
    }
}
