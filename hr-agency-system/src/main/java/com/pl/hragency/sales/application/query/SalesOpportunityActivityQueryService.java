package com.pl.hragency.sales.application.query;

import com.pl.hragency.sales.adapter.persistence.SalesOpportunityActivityReadJpaEntity;
import com.pl.hragency.sales.adapter.persistence.SalesOpportunityActivityReadSpecifications;
import com.pl.hragency.sales.adapter.persistence.SpringDataSalesOpportunityActivityReadRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SalesOpportunityActivityQueryService  {
    private final SpringDataSalesOpportunityActivityReadRepository repository;

    public SalesOpportunityActivityQueryService(SpringDataSalesOpportunityActivityReadRepository repository) {
        this.repository = repository;
    }

    public Page<SalesOpportunityActivityItem> search(
            UUID organizationId,
            SalesOpportunityActivityQuery query,
            Pageable pageable
    ) {
        var specifications = Specification.allOf(
                SalesOpportunityActivityReadSpecifications.organizationId(organizationId),
                SalesOpportunityActivityReadSpecifications.occurredFrom(query.occurredFromAtInstant()),
                SalesOpportunityActivityReadSpecifications.occurredTo(query.occurredToAtInstant()),
                SalesOpportunityActivityReadSpecifications.search(query.search()),
                SalesOpportunityActivityReadSpecifications.salesOpportunityId(query.salesOpportunityId()),
                SalesOpportunityActivityReadSpecifications.type(query.type())

        );

        return repository.findAll(specifications, pageable)
                .map(SalesOpportunityActivityQueryService::from);
    }

    public static SalesOpportunityActivityItem from(SalesOpportunityActivityReadJpaEntity entity) {
        return new SalesOpportunityActivityItem(
                entity.getId(),
                entity.getSalesOpportunityId(),
                entity.getType(),
                entity.getNote(),
                entity.getOccurredAt(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getCreatedFullName()
        );
    }
}
