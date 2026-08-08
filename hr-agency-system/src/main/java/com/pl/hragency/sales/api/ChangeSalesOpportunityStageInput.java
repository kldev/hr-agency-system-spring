package com.pl.hragency.sales.api;

import java.util.UUID;

public record ChangeSalesOpportunityStageInput(

        UUID salesOpportunityId,

        String stage,

        String lostReason
) {
}
