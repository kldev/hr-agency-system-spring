package com.pl.hragency.sales.application.command;

import com.pl.hragency.sales.domain.model.SalesOpportunityStage;


public record ChangeSalesOpportunityStageCommand(
        SalesOpportunityStage stage,

        String lostReason
) {
}
