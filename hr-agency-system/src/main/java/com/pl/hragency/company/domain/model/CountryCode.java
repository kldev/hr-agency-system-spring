package com.pl.hragency.company.domain.model;

public record CountryCode(String value) {

    public CountryCode {
        if (value == null || !value.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException(
                    "Country code must be ISO 3166-1 alpha-2"
            );
        }
    }
}
