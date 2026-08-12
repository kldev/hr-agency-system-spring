package com.pl.hragency.jobdescription.adapter.rest;

import com.pl.hragency.company.api.CompanyApi;
import com.pl.hragency.company.application.query.CompanyListItem;
import com.pl.hragency.company.application.query.CompanyListQuery;
import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.jobdescription.application.query.JobDescriptionItem;
import com.pl.hragency.jobdescription.application.query.JobDescriptionListQuery;
import com.pl.hragency.jobdescription.application.query.JobDescriptionQueryService;
import com.pl.hragency.shared.rest.ExecutionContext;
import com.pl.hragency.shared.rest.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-description")
@Tag(name = "Job Description")
public class JobDescriptionQueryController {
    private final JobDescriptionQueryService  jobDescriptionQueryService;
    private final IdentityApi identityApi;
    private final CompanyApi companyApi;

    public JobDescriptionQueryController(JobDescriptionQueryService jobDescriptionQueryService,
                                         IdentityApi identityApi,
                                         CompanyApi companyApi) {
        this.jobDescriptionQueryService = jobDescriptionQueryService;
        this.identityApi = identityApi;
        this.companyApi = companyApi;
    }

    private ExecutionContext getContext() {
        CurrentUser currentUser = identityApi.getCurrentUser();
        return currentUser.getExecutionContext();
    }

    @GetMapping
    public PageResponse<JobDescriptionItem> getPaged(
            @RequestParam(required = false) String search, @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size) {

        var pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        new Sort.Order(Sort.Direction.ASC, "title")
                )
        );

        var result = jobDescriptionQueryService.search(getContext().organizationId(),
                new JobDescriptionListQuery(search, pageable)
        );

        return new PageResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @GetMapping("company/{companyId}")
    public List<JobDescriptionItem> getForCompany(@PathVariable UUID companyId) {

        if (!companyApi.exists(getContext().organizationId(),companyId))
            throw new AccessDeniedException("Company does not belong to your organization");

        return jobDescriptionQueryService.findByCompany(getContext().organizationId(), companyId);
    }
}
