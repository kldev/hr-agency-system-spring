package com.pl.hragency.recruitment.adapter.persistence;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataJobApplicationNoteRepository extends JpaRepository<JobApplicationNoteJpaEntity, UUID> {
    Optional<JobApplicationNoteJpaEntity> findByIdAndOrganizationId(UUID id, UUID organizationId);
    List<JobApplicationNoteJpaEntity> findAllByOrganizationIdAndApplicationIdOrderByCreatedAtDesc(UUID id, UUID applicationId);
}
