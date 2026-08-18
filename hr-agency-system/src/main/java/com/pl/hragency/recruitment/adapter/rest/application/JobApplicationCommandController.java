package com.pl.hragency.recruitment.adapter.rest.application;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.recruitment.application.command.ChangeJobApplicationStatusCommand;
import com.pl.hragency.recruitment.application.command.CreateJobApplicationCommand;
import com.pl.hragency.recruitment.application.handler.ChangeJobApplicationStatusHandler;
import com.pl.hragency.recruitment.application.handler.CreateJobApplicationHandler;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationId;
import com.pl.hragency.recruitment.application.result.ApplyForPostingResult;
import com.pl.hragency.shared.rest.ApiResult;
import com.pl.hragency.shared.rest.ExecutionContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/recruitment/job-applications")
@Tag(name = "Recruitment - Job Applications")
public class JobApplicationCommandController {
    private final IdentityApi  identityApi;
    private final ChangeJobApplicationStatusHandler statusHandler;
    private final CreateJobApplicationHandler createHandler;

    public JobApplicationCommandController(IdentityApi identityApi,
                                           ChangeJobApplicationStatusHandler statusHandler,
                                           CreateJobApplicationHandler createHandler) {
        this.identityApi = identityApi;
        this.statusHandler = statusHandler;
        this.createHandler = createHandler;
    }

    private ExecutionContext getExecutionContext() {
        return identityApi.getCurrentUser().getExecutionContext();
    }

    @PostMapping
    private ApplyForPostingResult createJobApplication(@Validated @RequestBody CreateJobApplicationCommand command) {
        return createHandler.handle(getExecutionContext(), command);
    }

    @PutMapping("{jobApplicationId}/status")
    private ApiResult updateStatus(@PathVariable UUID jobApplicationId, @Validated @RequestBody ChangeJobApplicationStatusCommand command) {

        statusHandler.handle(getExecutionContext(), new JobApplicationId(jobApplicationId), command);

        return new ApiResult("Status updated", true);
    }
}
