package com.pl.hragency.recruitment.application.handler;

import com.pl.hragency.recruitment.application.port.CandidateQueryRepository;
import com.pl.hragency.recruitment.application.query.CandidateItem;
import com.pl.hragency.recruitment.application.query.CandidateListQuery;
import com.pl.hragency.shared.rest.ExecutionContext;
import com.pl.hragency.shared.rest.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetCandidatesListHandler {
    private final CandidateQueryRepository queryRepository;

    public GetCandidatesListHandler(CandidateQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public PageResponse<CandidateItem> execute(ExecutionContext context, CandidateListQuery query, Pageable pageable) {

        return queryRepository.search(context.organizationId(), query, pageable);
    }
}
