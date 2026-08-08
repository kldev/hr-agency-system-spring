package com.pl.hragency.company.adapter.persistence;

import java.util.UUID;

public record CompanySuggestionProjection(UUID id, String name, String taxId, String countryCode) {
}
