package com.pl.hragency.company.adapter.rest;

import com.pl.hragency.company.application.command.AssignSalesOwnerCommand;
import com.pl.hragency.company.application.command.CreateCompanyCommand;
import com.pl.hragency.company.application.query.*;
import com.pl.hragency.company.application.service.AssignSalesPersonHandler;
import com.pl.hragency.company.application.service.CreateCompanyHandler;
import com.pl.hragency.company.domain.model.CompanyContactCompanyId;
import com.pl.hragency.company.domain.model.CompanyId;
import com.pl.hragency.company.domain.model.CompanyOrganizationId;
import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.shared.rest.ApiResult;
import com.pl.hragency.shared.rest.ExecutionContext;
import com.pl.hragency.shared.rest.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/company")
@Tag(name = "Company")
public class CompanyController {
    private final CreateCompanyHandler createCompanyHandler;
    private final AssignSalesPersonHandler  assignSalesPersonHandler;
    private final CompanyQueryService companyQueryService;
    private final CompanyContactQueryService companyContactQueryService;
    private final IdentityApi identityApi;

    public CompanyController(CreateCompanyHandler createCompanyHandler, AssignSalesPersonHandler assignSalesPersonHandler,
                             CompanyQueryService companyQueryService, CompanyContactQueryService companyContactQueryService, IdentityApi identityApi) {
        this.createCompanyHandler = createCompanyHandler;
        this.assignSalesPersonHandler = assignSalesPersonHandler;
        this.companyQueryService = companyQueryService;
        this.companyContactQueryService = companyContactQueryService;
        this.identityApi = identityApi;
    }

    private ExecutionContext getContext() {
        CurrentUser currentUser = identityApi.getCurrentUser();
        return new ExecutionContext(currentUser.organizationId(), currentUser.userId());
    }

    @PostMapping
    public ResponseEntity<CompanyId> save(@RequestBody @Valid CreateCompanyCommand command) {
        ExecutionContext context =getContext();

        var companyId = createCompanyHandler.handle(context, command, identityApi.isCurrentUserSales());

        return ResponseEntity.ok(companyId);
    }

    @PostMapping("{companyId}/assign-sales")
    public ResponseEntity<ApiResult> assignSales(@PathVariable UUID companyId, @Valid @RequestBody AssignSalesOwnerCommand command) {

        assignSalesPersonHandler.handle(new CompanyId(companyId), command);

        return ResponseEntity.ok(new ApiResult("Sales person assigned successfully", true));
    }

    @GetMapping
    public PageResponse<CompanyListItem> getCompanies(
            @RequestParam(required = false) String search, @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size) {

        var pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        new Sort.Order(Sort.Direction.ASC, "name")
                )
        );

        var result = companyQueryService.findAll(getContext().organizationId(),
                new CompanyListQuery(search, pageable)
        );

        return new PageResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @GetMapping("{companyId}")
   public ResponseEntity<CompanyDetailsItem> getCompany(@PathVariable UUID companyId) {

        ExecutionContext context =getContext();
        var company = companyQueryService.findOne(new CompanyId(companyId), new CompanyOrganizationId(context.organizationId()))
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        var contacts = companyContactQueryService.findByCompanyId(new CompanyContactCompanyId(companyId));
        var detailsItem = new CompanyDetailsItem(company, contacts);

        return ResponseEntity.ok(detailsItem);
    }

}
