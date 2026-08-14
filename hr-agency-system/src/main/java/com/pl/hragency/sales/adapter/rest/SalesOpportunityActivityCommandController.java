package com.pl.hragency.sales.adapter.rest;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.sales.application.command.CreateSalesOpportunityActivityCommand;
import com.pl.hragency.sales.application.handler.CreateSalesOpportunityActivityHandler;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;
import com.pl.hragency.shared.rest.ExecutionContext;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sales/activity")
@Tag(name = "Sales")
public class SalesOpportunityActivityCommandController {
    private final CreateSalesOpportunityActivityHandler handler;
    private final IdentityApi identityApi;

    public SalesOpportunityActivityCommandController(CreateSalesOpportunityActivityHandler handler, IdentityApi identityApi) {
        this.handler = handler;
        this.identityApi = identityApi;
    }

    private ExecutionContext getContext() {
        var currentUser = identityApi.getCurrentUser();
        return currentUser.getExecutionContext();
    }

    @PostMapping("{opportunityId}")
    public UUID createActivity(@PathVariable UUID opportunityId, @RequestBody CreateSalesOpportunityActivityCommand command) {
        return handler.handle(getContext(), new SalesOpportunityId(opportunityId), command).value();
    }
}
