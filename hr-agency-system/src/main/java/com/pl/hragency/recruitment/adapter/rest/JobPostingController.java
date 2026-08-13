package com.pl.hragency.recruitment.adapter.rest;


import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.recruitment.application.command.ChangeJobPostingRecruiterCommand;
import com.pl.hragency.recruitment.application.command.ChangeJobPostingStatusCommand;
import com.pl.hragency.recruitment.application.command.CreateJobPostingCommand;
import com.pl.hragency.recruitment.application.command.UpdateJobPostingCommand;
import com.pl.hragency.recruitment.application.handler.ChangeJobPostingStatusHandler;
import com.pl.hragency.recruitment.application.handler.CreateJobPostingHandler;
import com.pl.hragency.recruitment.application.handler.UpdateJobPostingHandler;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.recruitment.application.handler.ChangeJobPostingRecruiterHandler;
import com.pl.hragency.shared.rest.ApiResult;
import com.pl.hragency.shared.rest.ExecutionContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/recruitment/job-posting")
@Tag(name = "Recruitment")
public class JobPostingController {
    private final ChangeJobPostingRecruiterHandler changeJobPostingRecruiterHandler;
    private final CreateJobPostingHandler  createJobPostingHandler;
    private final UpdateJobPostingHandler updateJobPostingHandler;
    private final ChangeJobPostingStatusHandler changeJobPostingStatusHandler;
    private final IdentityApi identityApi;


    private ExecutionContext getExecutionContext() {
        return identityApi.getCurrentUser().getExecutionContext();
    }

    public JobPostingController(ChangeJobPostingRecruiterHandler changeJobPostingRecruiterHandler,
                                CreateJobPostingHandler createJobPostingHandler,
                                UpdateJobPostingHandler updateJobPostingHandler,
                                ChangeJobPostingStatusHandler changeJobPostingStatusHandler,
                                IdentityApi identityApi) {

        this.changeJobPostingRecruiterHandler = changeJobPostingRecruiterHandler;
        this.createJobPostingHandler = createJobPostingHandler;
        this.updateJobPostingHandler = updateJobPostingHandler;
        this.changeJobPostingStatusHandler = changeJobPostingStatusHandler;
        this.identityApi = identityApi;
    }


    @PostMapping("")
    public JobPostingId createJobPosting(@RequestBody CreateJobPostingCommand command) {
        return createJobPostingHandler.handle(getExecutionContext(), command);
    }

    @PutMapping("{jobPostingId}")
    public ApiResult updateJobPosting(@PathVariable UUID jobPostingId, @RequestBody UpdateJobPostingCommand command) {
        updateJobPostingHandler.handle(getExecutionContext(), new JobPostingId(jobPostingId), command);
        return new ApiResult("Job Posting updated", true);
    }

    @PutMapping("{jobPostingId}/status")
    public ApiResult updateJobPostingStatus(@PathVariable UUID jobPostingId, @RequestBody ChangeJobPostingStatusCommand command) {
        changeJobPostingStatusHandler.handle(getExecutionContext(), new JobPostingId(jobPostingId), command);
        return new ApiResult("Job Posting updated", true);
    }

    @PutMapping("{jobPostingId}/recruiter")
    public ApiResult updateJobPostingStatus(@PathVariable UUID jobPostingId, @RequestBody ChangeJobPostingRecruiterCommand command) {
        changeJobPostingRecruiterHandler.handle(getExecutionContext(), new JobPostingId(jobPostingId), command);
        return new ApiResult("Job Posting updated", true);
    }
}
