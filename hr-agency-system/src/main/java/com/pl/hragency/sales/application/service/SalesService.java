package com.pl.hragency.sales.application.service;


import com.pl.hragency.sales.api.ChangeSalesOpportunityStageInput;
import com.pl.hragency.sales.api.CreateSalesOpportunityInput;
import com.pl.hragency.sales.api.SalesApi;
import com.pl.hragency.sales.application.command.ChangeSalesOpportunityStageCommand;
import com.pl.hragency.sales.application.command.CreateSalesOpportunityActivityCommand;
import com.pl.hragency.sales.application.command.CreateSalesOpportunityCommand;
import com.pl.hragency.sales.application.handler.ChangeSalesOpportunityStageHandler;
import com.pl.hragency.sales.application.handler.CreateSalesOpportunityActivityHandler;
import com.pl.hragency.sales.application.handler.CreateSalesOpportunityHandler;
import com.pl.hragency.sales.domain.model.SalesActivityType;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;
import com.pl.hragency.sales.domain.model.SalesOpportunityStage;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SalesService implements SalesApi {

    private final CreateSalesOpportunityHandler createHandler;
    private final ChangeSalesOpportunityStageHandler changeStageHandler;
    private final CreateSalesOpportunityActivityHandler createActivityHandler;

    public SalesService(CreateSalesOpportunityHandler createHandler,
                        ChangeSalesOpportunityStageHandler changeStageHandler, CreateSalesOpportunityActivityHandler createActivityHandler) {
        this.createHandler = createHandler;
        this.changeStageHandler = changeStageHandler;
        this.createActivityHandler = createActivityHandler;
    }

    @Override
    public UUID createOpportunity(UUID organizationId, UUID userId, CreateSalesOpportunityInput input) {
        return  createHandler.handle(
                new ExecutionContext(
                        organizationId,
                        userId,
                        "System"
                ),
                new CreateSalesOpportunityCommand(
                        input.companyId(),
                        input.title(),
                        input.description(),
                        input.expectedValue(),
                        input.currencyCode(),
                        input.expectedCloseDate(),
                        input.salesOwnerId()
                )
        ).value();
    }

    @Override
    public void changeOpportunityStage(UUID organizationId, UUID userId, ChangeSalesOpportunityStageInput input) {
        changeStageHandler.handle(
                new ExecutionContext(
                        organizationId,
                        userId,
                        "System"
                ),
                new SalesOpportunityId(input.salesOpportunityId()),
                new ChangeSalesOpportunityStageCommand(
                        SalesOpportunityStage.from(input.stage()),
                        input.lostReason()
                )
        );
    }

    @Override
    public void createActivity(UUID organizationId, UUID userId, UUID salesOpportunityId, String note, String activityType) {
        createActivityHandler.handle(new ExecutionContext(
                        organizationId,
                        userId,
                        "System"
                ), new SalesOpportunityId(salesOpportunityId),
                new CreateSalesOpportunityActivityCommand(note, SalesActivityType.valueOf(activityType)));
    }
}
