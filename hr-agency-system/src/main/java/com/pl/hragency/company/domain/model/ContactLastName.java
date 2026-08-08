package com.pl.hragency.company.domain.model;

public record ContactLastName(String value) {

    public ContactLastName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Last name cannot be empty"
            );
        }

        value = value.trim();

        if (value.length() > 100) {
            throw new IllegalArgumentException(
                    "Last name cannot exceed 100 characters"
            );
        }
    }
}
