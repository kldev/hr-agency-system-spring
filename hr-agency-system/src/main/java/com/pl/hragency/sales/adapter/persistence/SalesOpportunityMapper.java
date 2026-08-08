package com.pl.hragency.sales.adapter.persistence;

import com.pl.hragency.sales.domain.model.SalesOpportunity;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;

public final class SalesOpportunityMapper {

    private SalesOpportunityMapper() {
    }

    public static SalesOpportunityJpaEntity toEntity(
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

    public static SalesOpportunity toDomain(
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