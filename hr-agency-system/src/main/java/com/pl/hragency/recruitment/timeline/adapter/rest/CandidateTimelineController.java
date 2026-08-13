package com.pl.hragency.recruitment.timeline.adapter.rest;

import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.recruitment.timeline.application.handler.GetCandidateTimelineHandler;
import com.pl.hragency.recruitment.timeline.model.CandidateTimelineEntry;
import com.pl.hragency.shared.rest.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/recruitment/candidates")
@Tag(name = "Recruitment")
public class CandidateTimelineController {
    private final IdentityApi identityApi;
    private final GetCandidateTimelineHandler handler;

    public CandidateTimelineController(IdentityApi identityApi, GetCandidateTimelineHandler repository) {
        this.identityApi = identityApi;
        this.handler = repository;
    }

    @GetMapping("{candidateId}/timeline")
    public PageResponse<CandidateTimelineEntry> timeline(@PathVariable UUID candidateId,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "100") int size) {

        CurrentUser user = identityApi.getCurrentUser();

        var pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "occurredAt"));

        var result = handler.handle(user.getExecutionContext(), candidateId, pageRequest);
        return new PageResponse<>(result.getContent(),
                page,
                size,
                result.getTotalElements(),
                result.getTotalPages());
    }
}
