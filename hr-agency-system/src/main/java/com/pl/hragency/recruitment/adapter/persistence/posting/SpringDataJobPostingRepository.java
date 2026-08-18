package com.pl.hragency.recruitment.adapter.persistence.posting;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataJobPostingRepository extends JpaRepository<JobPostingJpaEntity, UUID>, JpaSpecificationExecutor<JobPostingJpaEntity> {

    Optional<JobPostingJpaEntity> findByIdAndOrganizationId(UUID id, UUID organizationId);
    Optional<JobPostingJpaEntity> findBySlugAndOrganizationId(String slug, UUID organizationId);
}
