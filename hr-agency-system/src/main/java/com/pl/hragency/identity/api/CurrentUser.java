package com.pl.hragency.identity.api;

import com.pl.hragency.identity.domain.model.UserRole;

import java.util.UUID;

public record CurrentUser(UUID userId, UUID organizationId, String fullName, UserRole role) {
}
