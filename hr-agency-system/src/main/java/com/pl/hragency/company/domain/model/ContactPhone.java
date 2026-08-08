package com.pl.hragency.company.domain.model;

public record ContactPhone(String value) {

    public ContactPhone(String value) {
        if (value == null || value.isBlank()) {
            this.value = null;
            return;
        }

        value = value.trim();

        if (value.length() > 50) {
            throw new IllegalArgumentException(
                    "Phone cannot exceed 50 characters"
            );
        }
        this.value = value;
    }
}
