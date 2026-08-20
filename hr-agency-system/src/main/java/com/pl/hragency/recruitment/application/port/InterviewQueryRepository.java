package com.pl.hragency.recruitment.application.port;

import com.pl.hragency.recruitment.application.query.InterviewItem;
import com.pl.hragency.recruitment.application.query.InterviewListQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.UUID;

public interface InterviewQueryRepository {
    Slice<InterviewItem> search(
            UUID organizationId,
            InterviewListQuery query,
            Pageable pageable
    );

    long countSearch(
            UUID organizationId,
            InterviewListQuery query
    );
}
