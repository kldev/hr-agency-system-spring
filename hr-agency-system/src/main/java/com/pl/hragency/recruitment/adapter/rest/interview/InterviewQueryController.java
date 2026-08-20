package com.pl.hragency.recruitment.adapter.rest.interview;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.recruitment.application.port.InterviewQueryRepository;
import com.pl.hragency.recruitment.application.query.InterviewItem;
import com.pl.hragency.recruitment.application.query.InterviewListQuery;
import com.pl.hragency.shared.rest.ExecutionContext;
import com.pl.hragency.shared.rest.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;

@RestController
@RequestMapping("/api/recruitment/interviews")
@Tag(name = "Recruitment - Interviews")
public class InterviewQueryController {

    private final InterviewQueryRepository repository;
    private final IdentityApi identityApi;

    public InterviewQueryController(InterviewQueryRepository repository, IdentityApi identityApi) {
        this.repository = repository;
        this.identityApi = identityApi;
    }

    private ExecutionContext getExecutionContext() {
        return identityApi.getCurrentUser().getExecutionContext();
    }


    @GetMapping
    public Slice<InterviewItem> getInterviews(@Validated @RequestParam(defaultValue = "0", required = false)  int page,
                                              @RequestParam(defaultValue = "20", required = false) @Max(500) int size,
                                              @RequestParam(defaultValue = "Europe/Warsaw", required = false) ZoneId timezone,
                                              @RequestParam(required = false) LocalDate from,
                                              @RequestParam(required = false) LocalDate to,
                                              @RequestParam(required = false) boolean onlyMine,
                                              @RequestParam(required = false) String search
                                                     ) {


        var sortBy = PageRequest.of(page, size,
                Sort.by(Sort.Direction.ASC, "scheduledAt"));

        var query = new InterviewListQuery(
                onlyMine ? getExecutionContext().userId() : null,
                from, to, timezone, search);

        return repository.search(getExecutionContext().organizationId(), query, sortBy);
    }

    @GetMapping("count")
    public long getInterviewsCount(@Validated @RequestParam(defaultValue = "0", required = false)  int page,
                                              @RequestParam(defaultValue = "20", required = false) @Max(500) int size,
                                              @RequestParam(defaultValue = "Europe/Warsaw", required = false) ZoneId timezone,
                                              @RequestParam(required = false) LocalDate from,
                                              @RequestParam(required = false) LocalDate to,
                                              @RequestParam(required = false) boolean onlyMine,
                                              @RequestParam(required = false) String search
    ) {
        var query = new InterviewListQuery(
                onlyMine ? getExecutionContext().userId() : null,
                from, to, timezone, search);

        return repository.countSearch(getExecutionContext().organizationId(), query);
    }
}
