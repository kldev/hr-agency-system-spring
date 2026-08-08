package com.pl.hragency.identity.api;

import java.util.UUID;

public record UserSuggestion(UUID id,
                             String fullName,
                             String email) {
}
