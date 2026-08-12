package com.pl.hragency.shared.rest;

import java.util.UUID;

public record ExecutionContext(UUID organizationId,
                               UUID userId,
                               String fullName) {
}
