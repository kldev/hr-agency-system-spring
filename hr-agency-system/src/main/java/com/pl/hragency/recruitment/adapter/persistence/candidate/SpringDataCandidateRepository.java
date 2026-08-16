package com.pl.hragency.recruitment.adapter.persistence.candidate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataCandidateRepository extends JpaRepository<CandidateJpaEntity, UUID>, JpaSpecificationExecutor<CandidateJpaEntity> {
    Optional<CandidateJpaEntity> findByIdAndOrganizationId(UUID id, UUID organizationId);
    Optional<CandidateJpaEntity> findByEmailAndOrganizationId(String email, UUID organizationId);
    boolean existsByEmailAndOrganizationId(String email, UUID organizationId);
}
