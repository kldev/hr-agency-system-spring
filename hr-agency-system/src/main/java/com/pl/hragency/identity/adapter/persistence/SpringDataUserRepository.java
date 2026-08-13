package com.pl.hragency.identity.adapter.persistence;

import com.pl.hragency.identity.domain.model.OrganizationRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, UUID>, JpaSpecificationExecutor<UserJpaEntity> {
    Optional<UserJpaEntity> findByEmailAndOrganizationId(String email, UUID organizationId);
    Optional<UserJpaEntity> findByEmail(String email);

    Page<UserJpaEntity> findAllByOrganizationId(
            UUID organizationId,
            Pageable pageable);

    Page<UserJpaEntity> findAllByOrganizationIdAndRole(
            UUID organizationId,
            OrganizationRole role,
            Pageable pageable);

    Page<UserJpaEntity> findAllByOrganizationIdAndEmailContainingIgnoreCase(
            UUID organizationId,
            String email,
            Pageable pageable);

    boolean existsByIdAndOrganizationId(UUID id, UUID organizationId);
    Optional<UserJpaEntity> findByIdAndOrganizationId(UUID id, UUID organizationId);
}
