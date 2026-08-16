package com.pl.hragency.organization.domain.model;

import java.text.Normalizer;
import java.util.Locale;

public record OrganizationSlug(String value) {

    public OrganizationSlug {
        value = normalize(value);
    }

    public static OrganizationSlug from(String value) {
        return new OrganizationSlug(value);
    }

    private static String normalize(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Organization slug cannot be blank"
            );
        }

        String slug = Normalizer
                .normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        if (slug.isBlank()) {
            throw new IllegalArgumentException(
                    "Organization slug must contain letters or digits"
            );
        }

        return slug;
    }

    @Override
    public String toString() {
        return value;
    }
}