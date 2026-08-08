package com.pl.hragency.company.application.query;

import com.pl.hragency.company.api.CompanySuggestion;

import java.util.List;
import java.util.UUID;

public interface CompanySuggestionsQuery {
    List<CompanySuggestion> find(UUID organizationId, String search, String countryCode);
}
