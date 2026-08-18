package com.pl.hragency.recruitment.adapter.persistence.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SpringDataTagRepository extends JpaRepository<TagJpaEntity, UUID>, JpaSpecificationExecutor<TagJpaEntity> {
}
