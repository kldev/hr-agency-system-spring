package com.pl.hragency.recruitment.adapter.persistence.interview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SpringDataInterviewReadRepository extends JpaRepository<InterviewReadJpaEntity, UUID>,
        JpaSpecificationExecutor<InterviewReadJpaEntity> {
}
