package com.pl.hragency.identity.api;

import com.pl.hragency.identity.domain.model.UserRole;
import com.pl.hragency.shared.rest.ExecutionContext;

import java.util.UUID;

public record CurrentUser(UUID userId, UUID organizationId, String fullName, UserRole role) {
    public ExecutionContext  getExecutionContext() {
        return new ExecutionContext(organizationId, userId, fullName);
    }
}
