package com.pl.hragency.jobdescription.adapter.rest;

import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.jobdescription.application.command.CreateJobDescriptionCommand;
import com.pl.hragency.jobdescription.application.service.CreateJobDescriptionHandler;
import com.pl.hragency.jobdescription.domain.model.JobDescriptionId;
import com.pl.hragency.shared.rest.ExecutionContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job-description")
@Tag(name = "Job Description")
public class JobDescriptionController {

    private final CreateJobDescriptionHandler  createJobDescriptionHandler;
    private final IdentityApi identityApi;

    public JobDescriptionController(CreateJobDescriptionHandler createJobDescriptionHandler, IdentityApi identityApi) {
        this.createJobDescriptionHandler = createJobDescriptionHandler;
        this.identityApi = identityApi;
    }

    private ExecutionContext getContext() {
        CurrentUser currentUser = identityApi.getCurrentUser();
        return new ExecutionContext(currentUser.organizationId(), currentUser.userId());
    }

    @PostMapping
    public ResponseEntity<JobDescriptionId>  createJobDescription(@Valid @RequestBody CreateJobDescriptionCommand command) {

        var result = createJobDescriptionHandler.handle(getContext(), command);
        return ResponseEntity.ok(result);
    }
}
