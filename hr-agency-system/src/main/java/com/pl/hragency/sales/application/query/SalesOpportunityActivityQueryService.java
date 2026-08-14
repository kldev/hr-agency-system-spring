package com.pl.hragency.sales.application.query;

import com.pl.hragency.sales.adapter.persistence.SpringDataSalesOpportunityActivityRepository;
import com.pl.hragency.sales.application.port.SalesOpportunityActivityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SalesOpportunityActivityQueryService  {
    private final SpringDataSalesOpportunityActivityRepository repository;

    public SalesOpportunityActivityQueryService(SpringDataSalesOpportunityActivityRepository repository) {
        this.repository = repository;
    }

    public Page<SalesOpportunityActivityItem> search(
            UUID organizationId,
            SalesOpportunityActivityQuery query,
            Pageable pageable
    ) {
        return repository.search(organizationId, query, pageable);
    }
}
