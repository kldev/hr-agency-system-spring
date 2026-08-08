package com.pl.hragency.sales.adapter.persistence;

import com.pl.hragency.sales.application.port.SalesOpportunityRepository;
import com.pl.hragency.sales.domain.model.SalesOpportunity;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;
import com.pl.hragency.sales.domain.model.SalesOpportunityStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SalesOpportunityPersistenceAdapter
        implements SalesOpportunityRepository {

    private final SpringDataSalesOpportunityRepository repository;

    public SalesOpportunityPersistenceAdapter(
            SpringDataSalesOpportunityRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public void save(
            SalesOpportunity opportunity
    ) {
        repository.save(
                SalesOpportunityMapper.toEntity(opportunity)
        );
    }

    @Override
    public Optional<SalesOpportunity> findById(
            UUID organizationId,
            SalesOpportunityId id
    ) {
        return repository
                .findById(id.value())
                .filter(entity ->
                        entity.getOrganizationId()
                                .equals(organizationId)
                )
                .map(SalesOpportunityMapper::toDomain);
    }

    @Override
    public Page<SalesOpportunity> findAll(
            UUID organizationId,
            SalesOpportunityStage stage,
            Pageable pageable
    ) {
        var specification = Specification.allOf(
                SalesOpportunitySpecifications.organizationId(
                        organizationId
                ),
                SalesOpportunitySpecifications.stage(stage));

        return repository
                .findAll(
                        specification,
                        pageable
                )
                .map(SalesOpportunityMapper::toDomain);
    }

    @Override
    public Page<SalesOpportunity> findByCompanyId(
            UUID organizationId,
            UUID companyId,
            Pageable pageable
    ) {
        var specification = Specification.allOf(
                SalesOpportunitySpecifications.organizationId(
                        organizationId
                ),
                SalesOpportunitySpecifications.companyId(
                        companyId
                ));

        return repository
                .findAll(
                        specification,
                        pageable
                )
                .map(SalesOpportunityMapper::toDomain);
    }

    @Override
    public int updateStage(UUID organizationId, SalesOpportunityId opportunityId, SalesOpportunityStage stage, String lostReason) {
        return repository.updateStage(
                organizationId,
                opportunityId.value(),
                stage,
                lostReason
        );
    }
}