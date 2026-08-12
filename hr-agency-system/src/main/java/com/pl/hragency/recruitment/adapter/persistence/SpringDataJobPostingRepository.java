package com.pl.hragency.recruitment.adapter.persistence;


import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataJobPostingRepository extends JpaRepository<JobPostingJpaEntity, UUID>, JpaSpecificationExecutor<JobPostingJpaEntity> {

    Optional<JobPostingJpaEntity> findByIdAndOrganizationId(UUID id, UUID organizationId);

    boolean existsByIdAndOrganizationId(UUID id, UUID organizationId);

    @Modifying
    @Query("""
        update JobPostingJpaEntity j
            set j.status = :status,
                j.updatedAt = :updateAt
            where j.id = :id and j.organizationId = :organizationId
    """)
    int updateStatus(UUID id, UUID organizationId, JobPostingStatus status, Instant updateAt);

    /// TODO: version = j.version + 1 where version = :version
    @Modifying
    @Query("""
        update JobPostingJpaEntity j
            set j.recruiterId = :recruiterId,
                j.updatedAt = :updateAt
            where j.id = :id and j.organizationId = :organizationId
    """)
    int updateRecruiter(UUID id, UUID organizationId, UUID recruiterId,  Instant updateAt);
}
