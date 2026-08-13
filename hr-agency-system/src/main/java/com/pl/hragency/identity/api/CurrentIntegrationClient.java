package com.pl.hragency.identity.api;

import com.pl.hragency.shared.rest.ExecutionContext;

import java.util.UUID;

public record CurrentIntegrationClient(UUID clientId,
                                       UUID organizationId,
                                       String clientName) {
    public ExecutionContext getExecutionContext() {
        return new ExecutionContext(clientId(), organizationId(), clientName());
    }
}
