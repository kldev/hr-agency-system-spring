package com.pl.hragency.company.domain.model;

public record Address(
        CountryCode countryCode,
        String city,
        String street,
        String postalCode
) {
}
