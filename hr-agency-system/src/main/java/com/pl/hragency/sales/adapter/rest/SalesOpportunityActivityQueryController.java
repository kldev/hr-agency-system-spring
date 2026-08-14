package com.pl.hragency.sales.adapter.rest;

import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.sales.application.query.SalesOpportunityActivityItem;
import com.pl.hragency.sales.application.query.SalesOpportunityActivityQuery;
import com.pl.hragency.sales.application.query.SalesOpportunityActivityQueryService;
import com.pl.hragency.sales.domain.model.SalesActivityType;
import com.pl.hragency.shared.rest.ExecutionContext;
import com.pl.hragency.shared.rest.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/sales/activity")
@Tag(name = "Sales")
public class SalesOpportunityActivityQueryController {
    private final SalesOpportunityActivityQueryService queryService;
    private final IdentityApi  identityApi;

    public SalesOpportunityActivityQueryController(SalesOpportunityActivityQueryService queryService,
                                                   IdentityApi identityApi) {
        this.queryService = queryService;
        this.identityApi = identityApi;
    }

    private ExecutionContext getContext() {
        CurrentUser currentUser = identityApi.getCurrentUser();
        return currentUser.getExecutionContext();
    }

    @GetMapping
    public PageResponse<SalesOpportunityActivityItem> getPaged(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0", required = false)  int page,
            @RequestParam(defaultValue = "20", required = false) int size,
            @RequestParam(required = false) UUID salesOpportunityId,
            @RequestParam(required = false) SalesActivityType type,
            @RequestParam(required = false) LocalDate occurredFrom,
            @RequestParam(required = false)LocalDate occurredTo) {

        var pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        new Sort.Order(Sort.Direction.DESC, "occurredAt")
                )
        );

        var result = queryService.search(getContext().organizationId(),
                new SalesOpportunityActivityQuery(
                        salesOpportunityId,
                        type,
                        occurredFrom,
                        occurredTo, search), pageable
        );

        return new PageResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }
}
