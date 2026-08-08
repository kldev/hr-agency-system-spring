package com.pl.hragency.company.domain.model;

public record ContactEmail(String value) {

    public ContactEmail(String value) {
        if (value == null || value.isBlank()) {
            this.value = null;
            return;
        }

        value = value.trim().toLowerCase();

        if (value.length() > 320) {
            throw new IllegalArgumentException(
                    "Email cannot exceed 320 characters"
            );
        }

        if (!value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException(
                    "Invalid email address"
            );
        }
        this.value = value;
    }
}
