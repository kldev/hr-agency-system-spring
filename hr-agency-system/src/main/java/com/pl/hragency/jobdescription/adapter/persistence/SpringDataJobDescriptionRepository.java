package com.pl.hragency.jobdescription.adapter.persistence;

import com.pl.hragency.company.adapter.persistence.CompanyJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataJobDescriptionRepository extends JpaRepository<JobDescriptionJpaEntity, UUID>, JpaSpecificationExecutor<JobDescriptionJpaEntity> {
    Optional<JobDescriptionJpaEntity> findByIdAndOrganizationId(UUID id, UUID organizationId);
}
