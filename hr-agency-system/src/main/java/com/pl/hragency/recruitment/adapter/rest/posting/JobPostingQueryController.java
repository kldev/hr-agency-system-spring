package com.pl.hragency.recruitment.adapter.rest.posting;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.recruitment.application.query.JobPostingItem;
import com.pl.hragency.recruitment.application.query.JobPostingListQuery;
import com.pl.hragency.recruitment.application.query.GetJobPostingQueryHandler;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import com.pl.hragency.shared.rest.ExecutionContext;
import com.pl.hragency.shared.rest.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/recruitment/job-posting")
@Tag(name = "Recruitment - Job Postings")
public class JobPostingQueryController {
    private final GetJobPostingQueryHandler queryService;

    private final IdentityApi identityApi;

    public JobPostingQueryController(GetJobPostingQueryHandler queryService, IdentityApi identityApi) {
        this.queryService = queryService;
        this.identityApi = identityApi;
    }

    private ExecutionContext getContext() {
        return identityApi.getCurrentUser().getExecutionContext();
    }

    @GetMapping
    public PageResponse<JobPostingItem> getPaged(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) JobPostingStatus status,
            @RequestParam(required = false) UUID companyId,
            @RequestParam(required = false) UUID jobDescriptionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        var pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        new Sort.Order(Sort.Direction.ASC, "createdAt"),
                        new Sort.Order(Sort.Direction.ASC, "title")
                )
        );

        var result = queryService.search(getContext().organizationId(),
                new JobPostingListQuery(search, status, companyId, jobDescriptionId), pageable
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
