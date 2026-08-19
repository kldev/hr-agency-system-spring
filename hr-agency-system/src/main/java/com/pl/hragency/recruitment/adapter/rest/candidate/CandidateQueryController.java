package com.pl.hragency.recruitment.adapter.rest.candidate;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.recruitment.application.port.CandidateQueryRepository;
import com.pl.hragency.recruitment.application.query.CandidateItem;
import com.pl.hragency.recruitment.application.query.CandidateListQuery;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateStatus;
import com.pl.hragency.shared.rest.ExecutionContext;
import com.pl.hragency.shared.rest.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/recruitment/candidates")
@Tag(name = "Recruitment - Candidates")
public class CandidateQueryController {

    private final CandidateQueryRepository repository;
    private final IdentityApi identityApi;

    public CandidateQueryController(CandidateQueryRepository repository, IdentityApi identityApi) {
        this.repository = repository;
        this.identityApi = identityApi;
    }

    private ExecutionContext getExecutionContext() {
        return identityApi.getCurrentUser().getExecutionContext();
    }


    @GetMapping
    public PageResponse<CandidateItem> getCandidates(@RequestParam(defaultValue = "0", required = false)  int page,
                                                     @RequestParam(defaultValue = "20", required = false) int size,
                                                     @RequestParam(required = false) UUID companyId,
                                                     @RequestParam(required = false) CandidateStatus status,
                                                     @RequestParam(required = false) String search,
                                                     @RequestParam(required = false) Set<UUID> tags

    ) {

        var sortBy = PageRequest.of(page, size,
                Sort.by(Sort.Direction.ASC, "createdAt"));

        var query = new CandidateListQuery(search, companyId, tags, status);

        return repository.search(getExecutionContext().organizationId(), query, sortBy);
    }
}
