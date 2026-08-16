package com.pl.hragency.recruitment.adapter.rest.interview;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.recruitment.application.command.CreateInterviewCommand;
import com.pl.hragency.recruitment.application.handler.CreateInterviewHandler;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationId;
import com.pl.hragency.shared.rest.ExecutionContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/recruitment/job-applications")
@Tag(name = "Recruitment")
public class InterviewCommandController {
    private final CreateInterviewHandler handler;
    private final IdentityApi identityApi;
    private ExecutionContext getExecutionContext() {
        return identityApi.getCurrentUser().getExecutionContext();
    }


    public InterviewCommandController(CreateInterviewHandler handler, IdentityApi identityApi) {
        this.handler = handler;
        this.identityApi = identityApi;
    }


    @PostMapping("{applicationId}/schedule-interview")
    public UUID createInterview(@PathVariable UUID applicationId,@RequestBody CreateInterviewCommand command) {
        return handler.handle(getExecutionContext(), new JobApplicationId(applicationId), command);
    }
}
