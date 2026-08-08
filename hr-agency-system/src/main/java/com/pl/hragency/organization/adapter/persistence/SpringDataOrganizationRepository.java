package com.pl.hragency.organization.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataOrganizationRepository extends JpaRepository<OrganizationJpaEntity, UUID> {
    Optional<OrganizationJpaEntity> findBySlug(String slug);

    boolean existsBySlug(String slug);
    boolean existsById(UUID id);
}
