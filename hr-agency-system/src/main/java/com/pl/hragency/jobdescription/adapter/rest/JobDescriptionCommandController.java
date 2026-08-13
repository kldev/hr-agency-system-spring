package com.pl.hragency.jobdescription.adapter.rest;

import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.jobdescription.application.command.ChangeJobDescriptionStatusCommand;
import com.pl.hragency.jobdescription.application.command.CreateJobDescriptionCommand;
import com.pl.hragency.jobdescription.application.handler.ChangeJobDescriptionStatusHandler;
import com.pl.hragency.jobdescription.application.handler.CreateJobDescriptionHandler;
import com.pl.hragency.jobdescription.domain.model.JobDescriptionId;
import com.pl.hragency.shared.rest.ApiResult;
import com.pl.hragency.shared.rest.ExecutionContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/job-description")
@Tag(name = "Job Description")
public class JobDescriptionCommandController {

    private final CreateJobDescriptionHandler  createHandler;
    private final ChangeJobDescriptionStatusHandler changeStatusHandler;
    private final IdentityApi identityApi;

    public JobDescriptionCommandController(CreateJobDescriptionHandler createHandler,
                                           ChangeJobDescriptionStatusHandler changeStatusHandler,
                                           IdentityApi identityApi) {
        this.createHandler = createHandler;
        this.changeStatusHandler = changeStatusHandler;
        this.identityApi = identityApi;
    }

    private ExecutionContext getContext() {
        CurrentUser currentUser = identityApi.getCurrentUser();
        return currentUser.getExecutionContext();
    }

    @PostMapping
    public ResponseEntity<JobDescriptionId>  createJobDescription(@Valid @RequestBody CreateJobDescriptionCommand command) {

        var result = createHandler.handle(getContext(), command);
        return ResponseEntity.ok(result);
    }

    @PostMapping("{jobDescriptionId}/status")
    public ResponseEntity<ApiResult>  updateStatusJobDescription(@PathVariable UUID jobDescriptionId,
                                                                 @Valid @RequestBody ChangeJobDescriptionStatusCommand command) {

        changeStatusHandler.handle(getContext(),new JobDescriptionId(jobDescriptionId), command);
        return ResponseEntity.ok(new ApiResult("Status updated", true));
    }
}
