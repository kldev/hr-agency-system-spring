package com.pl.hragency.recruitment.adapter.rest;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.recruitment.application.command.CreateJobApplicationNoteCommand;
import com.pl.hragency.recruitment.application.handler.CreateJobApplicationNoteHandler;
import com.pl.hragency.recruitment.application.query.GetJobApplicationNotesQueryHandler;
import com.pl.hragency.recruitment.application.query.JobApplicationNoteItem;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationId;
import com.pl.hragency.shared.rest.ExecutionContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recruitment/job-applications")
@Tag(name = "Recruitment")
public class JobApplicationNoteController {
    private final CreateJobApplicationNoteHandler handler;
    private final GetJobApplicationNotesQueryHandler queryHandler;
    private final IdentityApi identityApi;

    public JobApplicationNoteController(CreateJobApplicationNoteHandler handler,
                                        GetJobApplicationNotesQueryHandler queryHandler,
                                        IdentityApi identityApi) {
        this.handler = handler;
        this.queryHandler = queryHandler;
        this.identityApi = identityApi;
    }

    private ExecutionContext getExecutionContext() {
        return identityApi.getCurrentUser().getExecutionContext();
    }

    @PostMapping("{applicationId}/notes")
    public UUID createJobApplicationNote(@PathVariable UUID applicationId,
                                         @Validated @RequestBody CreateJobApplicationNoteCommand command) {
        return handler.handle(getExecutionContext(),
                new JobApplicationId(applicationId), command);
    }

    @GetMapping("{applicationId}/notes")
    public List<JobApplicationNoteItem> getJobApplicationNotes(@PathVariable UUID applicationId) {
        return queryHandler.getJobApplicationNotes(getExecutionContext().organizationId(),
                new JobApplicationId(applicationId));
    }
}
