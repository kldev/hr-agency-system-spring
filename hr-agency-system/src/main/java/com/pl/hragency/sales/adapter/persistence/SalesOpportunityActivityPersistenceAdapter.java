package com.pl.hragency.sales.adapter.persistence;

import com.pl.hragency.sales.application.port.SalesOpportunityActivityRepository;
import com.pl.hragency.sales.application.query.SalesOpportunityActivityQuery;
import com.pl.hragency.sales.domain.model.SalesOpportunityActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class SalesOpportunityActivityPersistenceAdapter implements SalesOpportunityActivityRepository {
    private final SpringDataSalesOpportunityActivityRepository repository;
    private final SalesOpportunityActivityMapper mapper;

    public SalesOpportunityActivityPersistenceAdapter(SpringDataSalesOpportunityActivityRepository repository,
                                                      SalesOpportunityActivityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(SalesOpportunityActivity salesOpportunityActivity) {
        repository.save(mapper.toEntity(salesOpportunityActivity));
    }

    @Override
    public Optional<SalesOpportunityActivity> findById(UUID id, UUID organizationId) {

        return repository.findByIdAndOrganizationId(id, organizationId).map(mapper::toDomain);
    }
}
