package com.pl.hragency.recruitment.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataJobApplicationRepository extends JpaRepository<JobApplicationJpaEntity, UUID>, JpaSpecificationExecutor<JobApplicationJpaEntity> {
    Optional<JobApplicationJpaEntity> findByCandidateIdAndJobPostingIdAndOrganizationId(UUID candidateId, UUID jobPostingId, UUID organizationId);
    Optional<JobApplicationJpaEntity> findByIdAndOrganizationId(UUID id, UUID organizationId);
}
