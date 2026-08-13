package com.pl.hragency.recruitment.domain.model.candidate;

import java.util.Locale;

public record CandidateEmail(String value) {

    public CandidateEmail {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        value = value.trim().toLowerCase(Locale.ROOT);
    }
}
