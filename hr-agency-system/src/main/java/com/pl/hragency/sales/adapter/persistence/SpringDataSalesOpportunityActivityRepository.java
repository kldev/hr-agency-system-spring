package com.pl.hragency.sales.adapter.persistence;

import com.pl.hragency.sales.application.query.SalesOpportunityActivityItem;
import com.pl.hragency.sales.application.query.SalesOpportunityActivityQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataSalesOpportunityActivityRepository extends JpaRepository<SalesOpportunityActivityJpaEntity, UUID>, JpaSpecificationExecutor<SalesOpportunityActivityJpaEntity> {
    Optional<SalesOpportunityActivityJpaEntity> findByIdAndOrganizationId(UUID id, UUID organizationId);
}
