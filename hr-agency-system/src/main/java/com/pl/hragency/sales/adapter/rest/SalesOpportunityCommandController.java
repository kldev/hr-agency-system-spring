package com.pl.hragency.sales.adapter.rest;

import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.api.IdentityApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pl.hragency.sales.application.command.ChangeSalesOpportunityStageCommand;
import com.pl.hragency.sales.application.command.CreateSalesOpportunityCommand;
import com.pl.hragency.sales.application.service.ChangeSalesOpportunityStageHandler;
import com.pl.hragency.sales.application.service.CreateSalesOpportunityHandler;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales/opportunity")
@Tag(name = "Sales")
public class SalesOpportunityCommandController {
    private final CreateSalesOpportunityHandler createHandler;
    private final ChangeSalesOpportunityStageHandler changeStageHandler;
    private final IdentityApi identityApi;

    public SalesOpportunityCommandController(CreateSalesOpportunityHandler createHandler, ChangeSalesOpportunityStageHandler changeStageHandler, IdentityApi identityApi) {
        this.createHandler = createHandler;
        this.changeStageHandler = changeStageHandler;
        this.identityApi = identityApi;
    }

    private ExecutionContext getContext() {
        CurrentUser currentUser = identityApi.getCurrentUser();
        return currentUser.getExecutionContext();
    }

    @PostMapping
    public SalesOpportunityId create(
            @RequestBody CreateSalesOpportunityCommand command
    ) {

        return createHandler.handle(
                getContext(),
                command
        );
    }

    @PatchMapping("/{opportunityId}/stage")
    public ResponseEntity<Void> changeStage(
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

        return ResponseEntity.noContent().build();
    }
}
