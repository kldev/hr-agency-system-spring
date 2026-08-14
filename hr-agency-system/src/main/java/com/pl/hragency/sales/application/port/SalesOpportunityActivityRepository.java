package com.pl.hragency.sales.application.port;

import com.pl.hragency.sales.domain.model.SalesOpportunityActivity;

import java.util.Optional;
import java.util.UUID;

public interface SalesOpportunityActivityRepository {
    void save(SalesOpportunityActivity salesOpportunityActivity);
    Optional<SalesOpportunityActivity> findById(UUID id, UUID organizationId);
}
