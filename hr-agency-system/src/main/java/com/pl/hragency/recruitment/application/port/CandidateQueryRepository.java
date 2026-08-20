package com.pl.hragency.recruitment.application.port;

import com.pl.hragency.recruitment.application.query.CandidateItem;
import com.pl.hragency.recruitment.application.query.CandidateListQuery;
import com.pl.hragency.shared.rest.SliceResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.UUID;

public interface CandidateQueryRepository {
    SliceResponse<CandidateItem> search(UUID organizationId, CandidateListQuery query, Pageable pageable);
    long countSearch(UUID organizationId, CandidateListQuery query);
}
