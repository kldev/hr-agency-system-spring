package com.pl.hragency.company.adapter.rest;

import com.pl.hragency.company.application.query.*;
import com.pl.hragency.company.domain.model.CompanyContactCompanyId;
import com.pl.hragency.company.domain.model.CompanyId;
import com.pl.hragency.company.domain.model.CompanyOrganizationId;
import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.shared.rest.ExecutionContext;
import com.pl.hragency.shared.rest.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/company")
@Tag(name = "Company")
public class CompanyQueryController {
    private final CompanyQueryService companyQueryService;
    private final CompanyContactQueryService companyContactQueryService;
    private final IdentityApi identityApi;

    private ExecutionContext getContext() {
        CurrentUser currentUser = identityApi.getCurrentUser();
        return currentUser.getExecutionContext();
    }

    public CompanyQueryController(CompanyQueryService companyQueryService, CompanyContactQueryService companyContactQueryService, IdentityApi identityApi) {
        this.companyQueryService = companyQueryService;
        this.companyContactQueryService = companyContactQueryService;
        this.identityApi = identityApi;
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
