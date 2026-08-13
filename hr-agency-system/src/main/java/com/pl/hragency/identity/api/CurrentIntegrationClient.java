package com.pl.hragency.identity.api;

import java.util.UUID;

public record CurrentIntegrationClient(UUID clientId,
                                       UUID organizationId,
                                       String clientName) {
}
