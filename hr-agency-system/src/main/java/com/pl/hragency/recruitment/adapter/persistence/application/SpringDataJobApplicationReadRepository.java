package com.pl.hragency.recruitment.adapter.persistence.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SpringDataJobApplicationReadRepository extends JpaRepository<JobApplicationReadJpaEntity, UUID>, JpaSpecificationExecutor<JobApplicationReadJpaEntity> {
}
