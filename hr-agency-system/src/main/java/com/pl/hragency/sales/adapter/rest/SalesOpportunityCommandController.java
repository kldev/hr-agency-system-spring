package com.pl.hragency.sales.adapter.rest;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.shared.rest.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pl.hragency.sales.application.command.ChangeSalesOpportunityStageCommand;
import com.pl.hragency.sales.application.command.CreateSalesOpportunityCommand;
import com.pl.hragency.sales.application.handler.ChangeSalesOpportunityStageHandler;
import com.pl.hragency.sales.application.handler.CreateSalesOpportunityHandler;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sales/opportunity")
@Tag(name = "Sales")
public class SalesOpportunityCommandController {
    private final CreateSalesOpportunityHandler createHandler;
    private final ChangeSalesOpportunityStageHandler changeStageHandler;
    private final IdentityApi identityApi;
    private ExecutionContext getContext() {
        var currentUser = identityApi.getCurrentUser();
        return currentUser.getExecutionContext();
    }

    public SalesOpportunityCommandController(CreateSalesOpportunityHandler createHandler,
                                             ChangeSalesOpportunityStageHandler changeStageHandler,
                                             IdentityApi identityApi) {
        this.createHandler = createHandler;
        this.changeStageHandler = changeStageHandler;
        this.identityApi = identityApi;
    }

    @PostMapping
    public UUID create(@Validated @RequestBody CreateSalesOpportunityCommand command) {
        return createHandler.handle(getContext(),command).value();
    }

    @PatchMapping("/{opportunityId}/stage")
    public ApiResult changeStage(
            @PathVariable("opportunityId")
            java.util.UUID id,

            @RequestBody
            ChangeSalesOpportunityStageCommand command
    ) {

        changeStageHandler.handle(
                getContext(),
                new SalesOpportunityId(id),
                command
        );

        return new ApiResult("Stage updated", true);
    }
}
