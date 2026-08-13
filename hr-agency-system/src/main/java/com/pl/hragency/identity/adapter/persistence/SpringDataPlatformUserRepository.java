package com.pl.hragency.identity.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataPlatformUserRepository extends JpaRepository<PlatformUserJpaEntity, UUID> {
    Optional<PlatformUserJpaEntity> findByEmail(String email);
}
