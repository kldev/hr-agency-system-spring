package com.pl.hragency.identity.application.port;

import com.pl.hragency.identity.domain.model.IntegrationClient;

import java.util.Optional;

public interface IntegrationClientRepository {
    void save(IntegrationClient integrationClient);
    Optional<IntegrationClient> findActiveByKeyId(String code);
}
