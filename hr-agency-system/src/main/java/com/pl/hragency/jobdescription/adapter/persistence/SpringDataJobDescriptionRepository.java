package com.pl.hragency.jobdescription.adapter.persistence;

import com.pl.hragency.company.adapter.persistence.CompanyJpaEntity;
import com.pl.hragency.jobdescription.domain.model.JobDescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataJobDescriptionRepository extends JpaRepository<JobDescriptionJpaEntity, UUID>, JpaSpecificationExecutor<JobDescriptionJpaEntity> {
    Optional<JobDescriptionJpaEntity> findByIdAndOrganizationId(UUID id, UUID organizationId);
    boolean existsByIdAndOrganizationId(UUID id, UUID organizationId);

    @Modifying
    @Query("""
        update JobDescriptionJpaEntity j
            set j.status = :status,
                j.updatedAt = :updateAt
            where j.id = :id and j.organizationId = :organizationId
    """)
    int updateStatus(UUID id, UUID organizationId, JobDescriptionStatus status, Instant updateAt);
}
