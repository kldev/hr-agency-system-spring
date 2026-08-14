package com.pl.hragency.sales.adapter.rest;

import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.sales.application.query.SalesOpportunityItem;
import com.pl.hragency.sales.application.query.SalesOpportunityQueryService;
import com.pl.hragency.sales.domain.model.SalesOpportunityStage;
import com.pl.hragency.shared.rest.ExecutionContext;
import com.pl.hragency.shared.rest.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sales/opportunity")
@Tag(name = "Sales")
public class SalesOpportunityQueryController {

    private final SalesOpportunityQueryService queryService;
    private final IdentityApi  identityApi;

    public SalesOpportunityQueryController(
            SalesOpportunityQueryService queryService,
            IdentityApi identityApi
    ) {
        this.queryService = queryService;
        this.identityApi = identityApi;
    }

    private ExecutionContext getContext() {
        CurrentUser currentUser = identityApi.getCurrentUser();
        return currentUser.getExecutionContext();
    }

    @GetMapping
    public PageResponse<SalesOpportunityItem> findAll(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size,
            @RequestParam(required = false)
            SalesOpportunityStage stage

    ) {
        var pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "createdAt"
                )
        );

        return PageResponse.from(
                queryService.findAll(
                        getContext().organizationId(),
                        stage,
                        pageable
                )
        );
    }

    @GetMapping("/company/{companyId}")
    public PageResponse<SalesOpportunityItem> findByCompany(
            @PathVariable UUID companyId,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size
    ) {
        var pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.DESC,
                        "createdAt"
                )
        );

        return PageResponse.from(
                queryService.findByCompany(
                        getContext().organizationId(),
                        companyId,
                        pageable
                )
        );
    }
}