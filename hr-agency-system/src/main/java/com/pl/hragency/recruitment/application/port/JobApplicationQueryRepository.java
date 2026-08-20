package com.pl.hragency.recruitment.application.port;

import com.pl.hragency.recruitment.application.query.JobApplicationItem;
import com.pl.hragency.recruitment.application.query.JobApplicationListQuery;
import com.pl.hragency.shared.rest.SliceResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface JobApplicationQueryRepository {

    SliceResponse<JobApplicationItem> search(UUID organizationId, JobApplicationListQuery query, Pageable pageable);
    long countSearch(UUID organizationId, JobApplicationListQuery query);
}
