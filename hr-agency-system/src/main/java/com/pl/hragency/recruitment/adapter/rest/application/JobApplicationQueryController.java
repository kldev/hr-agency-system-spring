package com.pl.hragency.recruitment.adapter.rest.application;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.recruitment.application.port.JobApplicationQueryRepository;
import com.pl.hragency.recruitment.application.query.JobApplicationItem;
import com.pl.hragency.recruitment.application.query.JobApplicationListQuery;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationStatus;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import com.pl.hragency.shared.rest.ExecutionContext;
import com.pl.hragency.shared.rest.SliceResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/recruitment/job-applications")
@Tag(name = "Recruitment - Job Applications")
public class JobApplicationQueryController {

    private final JobApplicationQueryRepository repository;
    private final IdentityApi identityApi;

    public JobApplicationQueryController(JobApplicationQueryRepository repository, IdentityApi identityApi) {
        this.repository = repository;
        this.identityApi = identityApi;
    }

    private ExecutionContext getExecutionContext() {
        return identityApi.getCurrentUser().getExecutionContext();
    }

    @GetMapping
    public SliceResponse<JobApplicationItem> getApplications(@Validated @RequestParam(defaultValue = "0", required = false)  int page,
                                                             @RequestParam(defaultValue = "20", required = false)@Max(500) int size,
                                                             @RequestParam(required = false) UUID companyId,
                                                             @RequestParam(required = false) UUID postingId,
                                                             @RequestParam(required = false) UUID recruiterId,
                                                             @RequestParam(required = false) CandidateSource source,
                                                             @RequestParam(required = false) String search,
                                                             @RequestParam(required = false) JobApplicationStatus status

    ) {

        var sortBy = PageRequest.of(page, size,
                Sort.by(Sort.Direction.ASC, "createdAt"));

        var query = new JobApplicationListQuery(search, companyId, status, postingId,recruiterId, source);

        return repository.search(getExecutionContext().organizationId(), query, sortBy);
    }

    @GetMapping("count")
    public long getCountApplications(@RequestParam(required = false) UUID companyId,
                                                    @RequestParam(required = false) UUID postingId,
                                                    @RequestParam(required = false) UUID recruiterId,
                                                    @RequestParam(required = false) CandidateSource source,
                                                    @RequestParam(required = false) String search,
                                                    @RequestParam(required = false) JobApplicationStatus status

    ) {
        var query = new JobApplicationListQuery(search, companyId, status, postingId,recruiterId, source);

        return repository.countSearch(getExecutionContext().organizationId(), query);
    }
}
