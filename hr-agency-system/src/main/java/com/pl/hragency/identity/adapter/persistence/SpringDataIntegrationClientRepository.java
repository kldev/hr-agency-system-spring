package com.pl.hragency.identity.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataIntegrationClientRepository extends JpaRepository<IntegrationClientJpaEntity, UUID> {
    Optional<IntegrationClientJpaEntity> findByKeyId(String keyId);
}
