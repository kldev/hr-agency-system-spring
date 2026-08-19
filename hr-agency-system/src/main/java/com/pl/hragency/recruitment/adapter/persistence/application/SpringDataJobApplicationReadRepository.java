package com.pl.hragency.recruitment.adapter.persistence.application;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SpringDataJobApplicationReadRepository extends JpaRepository<JobApplicationReadJpaEntity, UUID>, JpaSpecificationExecutor<JobApplicationReadJpaEntity> {
}
