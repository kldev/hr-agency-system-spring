package com.pl.hragency.sales.adapter.persistence;

import com.pl.hragency.sales.domain.model.SalesOpportunity;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;
import org.springframework.stereotype.Component;

@Component
public class SalesOpportunityMapper {

    protected SalesOpportunityMapper() {
    }

    public SalesOpportunityJpaEntity toEntity(
            SalesOpportunity opportunity
    ) {
        return new SalesOpportunityJpaEntity(
                opportunity.id().value(),
                opportunity.organizationId(),
                opportunity.companyId(),
                opportunity.title(),
                opportunity.description(),
                opportunity.stage(),
                opportunity.expectedValue(),
                opportunity.currencyCode(),
                opportunity.expectedCloseDate(),
                opportunity.lostReason(),
                opportunity.salesOwnerId(),
                opportunity.createdAt()
        );
    }

    public SalesOpportunity toDomain(
            SalesOpportunityJpaEntity entity
    ) {
        return SalesOpportunity.rehydrate(
                new SalesOpportunityId(entity.getId()),
                entity.getOrganizationId(),
                entity.getCompanyId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStage(),
                entity.getExpectedValue(),
                entity.getCurrencyCode(),
                entity.getExpectedCloseDate(),
                entity.getLostReason(),
                entity.getSalesOwnerId(),
                entity.getCreatedAt()
        );
    }
}