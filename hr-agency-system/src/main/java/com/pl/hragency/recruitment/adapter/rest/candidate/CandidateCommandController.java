package com.pl.hragency.recruitment.adapter.rest.candidate;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.recruitment.adapter.rest.candidate.model.CandidateResponse;
import com.pl.hragency.recruitment.application.command.*;
import com.pl.hragency.recruitment.application.handler.*;
import com.pl.hragency.recruitment.application.port.CandidateTaggingRepository;
import com.pl.hragency.recruitment.application.port.TagQueryRepository;
import com.pl.hragency.recruitment.application.query.CandidateTagItem;
import com.pl.hragency.recruitment.application.query.TagItem;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.shared.rest.ApiResult;
import com.pl.hragency.shared.rest.ExecutionContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recruitment/candidates")
@Tag(name = "Recruitment - Candidates")
public class CandidateCommandController {
    private final CreateCandidateHandler createCandidateHandler;
    private final UpdateCandidateHandler updateCandidateHandler;
    private final TagCandidateHandler tagCandidateHandler;
    private final RemoveCandidateTagHandler removeCandidateTagHandler;
    private final UpdateCandidateSummaryHandler updateCandidateSummaryHandler;
    private final CandidateTaggingRepository taggingRepository;
    private final IdentityApi  identityApi;

    private ExecutionContext getExecutionContext() {
        return identityApi.getCurrentUser().getExecutionContext();
    }

    public CandidateCommandController(CreateCandidateHandler createCandidateHandler,
                                      UpdateCandidateHandler updateCandidateHandler,
                                      TagCandidateHandler tagCandidateHandler,
                                      RemoveCandidateTagHandler removeCandidateTagHandler,
                                      UpdateCandidateSummaryHandler updateCandidateSummaryHandler,
                                      CandidateTaggingRepository taggingRepository,
                                      IdentityApi identityApi) {
        this.createCandidateHandler = createCandidateHandler;
        this.updateCandidateHandler = updateCandidateHandler;
        this.tagCandidateHandler = tagCandidateHandler;
        this.removeCandidateTagHandler = removeCandidateTagHandler;
        this.updateCandidateSummaryHandler = updateCandidateSummaryHandler;
        this.taggingRepository = taggingRepository;
        this.identityApi = identityApi;
    }

    @PostMapping
    public CandidateResponse createCandidate(@Validated @RequestBody CreateCandidateCommand command) {

        var candidate = createCandidateHandler.handle(getExecutionContext(), command);
        return  new CandidateResponse(candidate.id().value(), candidate.email());
    }

    @PutMapping("{candidateId}")
    public ApiResult updateCandidate(@PathVariable UUID candidateId, @Validated @RequestBody UpdateCandidateCommand command) {

        updateCandidateHandler.handle(getExecutionContext(), new CandidateId(candidateId), command);
        return new ApiResult("Candidate updated", true);
    }

    @PutMapping("{candidateId}/tag")
    public ApiResult addTag(@PathVariable UUID candidateId, @Validated @RequestBody TagCandidateCommand command) {

        tagCandidateHandler.execute(getExecutionContext(), candidateId, command);
        return new ApiResult("Candidate tag added", true);
    }

    @GetMapping("{candidateId}/tag")
    public List<CandidateTagItem> getTags(@PathVariable UUID candidateId) {

        return taggingRepository.getListOfTags(new CandidateId(candidateId));
    }

    @DeleteMapping("{candidateId}/tag/{tagId}")
    public ApiResult removeTag(@PathVariable UUID candidateId, @PathVariable UUID tagId) {

        var command = new RemoveCandidateTagCommand(tagId);

        removeCandidateTagHandler.execute(getExecutionContext(), candidateId, command);

        return new ApiResult("Candidate tag removed", true);
    }

    @PutMapping("{candidateId}/summary")
    public ApiResult addTag(@PathVariable UUID candidateId, @Validated @RequestBody UpdateCandidateSummaryCommand command) {

        updateCandidateSummaryHandler.execute(getExecutionContext(), candidateId, command);
        return new ApiResult("Candidate summary updated", true);
    }
}
