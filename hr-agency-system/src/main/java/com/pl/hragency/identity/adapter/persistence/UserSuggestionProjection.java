package com.pl.hragency.identity.adapter.persistence;

import java.util.UUID;

public record UserSuggestionProjection(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
