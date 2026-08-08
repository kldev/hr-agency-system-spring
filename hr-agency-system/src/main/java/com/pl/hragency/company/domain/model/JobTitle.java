package com.pl.hragency.company.domain.model;

public record JobTitle(String value) {

    public JobTitle(String value) {
        if (value == null || value.isBlank()) {
            this.value = null;
            return;
        }

        value = value.trim();

        if (value.length() > 150) {
            throw new IllegalArgumentException(
                    "Job title cannot exceed 150 characters"
            );
        }
        this.value = value;
    }
}
