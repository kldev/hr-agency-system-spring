package com.pl.hragency.recruitment.application.port;

import com.pl.hragency.recruitment.application.query.JobPostingItem;
import com.pl.hragency.recruitment.application.query.JobPostingListQuery;
import com.pl.hragency.shared.rest.SliceResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface JobPostingQueryRepository {
    SliceResponse<JobPostingItem> search(
            UUID organizationId,
            JobPostingListQuery query,
            Pageable pageable
    );

    long countSearch(
            UUID organizationId,
            JobPostingListQuery query
    );
}
