package com.pl.hragency.recruitment.adapter.rest;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.recruitment.application.command.CreateCandidateCommand;
import com.pl.hragency.recruitment.application.command.UpdateCandidateCommand;
import com.pl.hragency.recruitment.application.handler.CreateCandidateHandler;
import com.pl.hragency.recruitment.application.handler.UpdateCandidateHandler;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.shared.rest.ApiResult;
import com.pl.hragency.shared.rest.ExecutionContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/recruitment/candidates")
@Tag(name = "Recruitment")
public class CandidateCommandController {
    private final CreateCandidateHandler createCandidateHandler;
    private final UpdateCandidateHandler updateCandidateHandler;
    private final IdentityApi  identityApi;

    public CandidateCommandController(CreateCandidateHandler createCandidateHandler,
                                      UpdateCandidateHandler updateCandidateHandler,
                                      IdentityApi identityApi) {
        this.createCandidateHandler = createCandidateHandler;
        this.updateCandidateHandler = updateCandidateHandler;
        this.identityApi = identityApi;
    }

    private ExecutionContext getExecutionContext() {
        return identityApi.getCurrentUser().getExecutionContext();
    }

    @PostMapping
    public UUID createCandidate(@Validated @RequestBody CreateCandidateCommand command) {

        return createCandidateHandler.handle(getExecutionContext(), command).id().value();
    }

    @PutMapping("{candidateId}")
    public ApiResult updateCandidate(@PathVariable UUID candidateId, @Validated @RequestBody UpdateCandidateCommand command) {

        updateCandidateHandler.handle(getExecutionContext(), new CandidateId(candidateId), command);
        return new ApiResult("Candidate updated", true);
    }
}
