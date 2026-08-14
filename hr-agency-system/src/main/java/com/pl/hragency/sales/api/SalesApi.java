package com.pl.hragency.sales.api;

import java.util.UUID;

public interface SalesApi {

    UUID createOpportunity(
            UUID organizationId,
            UUID userId,
            CreateSalesOpportunityInput input
    );

    void changeOpportunityStage(
            UUID organizationId,
            UUID userId,
            ChangeSalesOpportunityStageInput input
    );

    void createActivity(UUID organizationId,
                        UUID userId,
                        UUID salesOpportunityId,
                        String note,
                        String activityType);
}
