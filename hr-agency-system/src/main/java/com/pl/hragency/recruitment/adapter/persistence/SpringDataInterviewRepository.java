package com.pl.hragency.recruitment.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataInterviewRepository  extends JpaRepository<InterviewJpaEntity, UUID> {

    Optional<InterviewJpaEntity> findByOrganizationIdAndId(UUID organizationId, UUID interviewId);
}
