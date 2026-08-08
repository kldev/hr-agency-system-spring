package com.pl.hragency.sales.application.port;

import com.pl.hragency.sales.domain.model.SalesOpportunity;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;
import com.pl.hragency.sales.domain.model.SalesOpportunityStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface SalesOpportunityRepository {

    void save(SalesOpportunity opportunity);

    Optional<SalesOpportunity> findById(
            UUID organizationId,
            SalesOpportunityId id
    );

    Page<SalesOpportunity> findAll(
            UUID organizationId,
            SalesOpportunityStage stage,
            Pageable pageable
    );

    Page<SalesOpportunity> findByCompanyId(
            UUID organizationId,
            UUID companyId,
            Pageable pageable
    );

    int updateStage(UUID organizationId, SalesOpportunityId opportunityId, SalesOpportunityStage  stage, String lostReason);
}
