package com.pl.hragency.company.api;

import java.util.UUID;

public record CompanySuggestion(UUID id, String name, String taxNumber, String country) {
}
