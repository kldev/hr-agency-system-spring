package com.pl.hragency.sales.adapter.persistence;

import com.pl.hragency.sales.domain.model.SalesOpportunityActivity;
import com.pl.hragency.sales.domain.model.SalesOpportunityActivityId;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;

import org.springframework.stereotype.Component;

@Component
public final class SalesOpportunityActivityMapper {

    private SalesOpportunityActivityMapper() {
    }

    public SalesOpportunityActivityJpaEntity toEntity(
            SalesOpportunityActivity activity) {

        return new SalesOpportunityActivityJpaEntity(
                activity.id().value(),
                activity.organizationId(),
                activity.salesOpportunityId().value(),
                activity.type(),
                activity.note(),
                activity.occurredAt(),
                activity.createdAt(),
                activity.createdBy()
        );
    }

    public SalesOpportunityActivity toDomain(
            SalesOpportunityActivityJpaEntity entity) {

        return SalesOpportunityActivity.rehydrate(
                new SalesOpportunityActivityId(entity.getId()),
                entity.getOrganizationId(),
                new SalesOpportunityId(entity.getId()),
                entity.getType(),
                entity.getNote(),
                entity.getOccurredAt(),
                entity.getCreatedAt(),
                entity.getCreatedBy()
        );
    }
}