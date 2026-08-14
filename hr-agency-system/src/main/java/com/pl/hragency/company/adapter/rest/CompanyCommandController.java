package com.pl.hragency.company.adapter.rest;

import com.pl.hragency.company.application.command.AssignSalesOwnerCommand;
import com.pl.hragency.company.application.command.CreateCompanyCommand;
import com.pl.hragency.company.application.handler.AssignSalesOwnerHandler;
import com.pl.hragency.company.application.handler.CreateCompanyHandler;
import com.pl.hragency.company.domain.model.CompanyId;
import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.shared.rest.ApiResult;
import com.pl.hragency.shared.rest.ExecutionContext;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/company")
@Tag(name = "Company")
public class CompanyCommandController {
    private final CreateCompanyHandler createCompanyHandler;
    private final AssignSalesOwnerHandler assignSalesPersonHandler;
    private final IdentityApi identityApi;

    public CompanyCommandController(CreateCompanyHandler createCompanyHandler,
                                    AssignSalesOwnerHandler assignSalesPersonHandler,
                                    IdentityApi identityApi) {
        this.createCompanyHandler = createCompanyHandler;
        this.assignSalesPersonHandler = assignSalesPersonHandler;
        this.identityApi = identityApi;
    }

    private ExecutionContext getContext() {
        CurrentUser currentUser = identityApi.getCurrentUser();
        return currentUser.getExecutionContext();
    }

    @PostMapping
    public UUID save(@RequestBody @Valid CreateCompanyCommand command) {
        ExecutionContext context =getContext();

        var companyId = createCompanyHandler.handle(context, command, identityApi.isCurrentUserSales());

        return companyId.value();
    }

    @PostMapping("{companyId}/assign-sales")
    public ApiResult assignSales(@PathVariable UUID companyId, @Valid @RequestBody AssignSalesOwnerCommand command) {

        assignSalesPersonHandler.handle(getContext(), new CompanyId(companyId), command);

        return new ApiResult("Sales person assigned successfully", true);
    }



}
