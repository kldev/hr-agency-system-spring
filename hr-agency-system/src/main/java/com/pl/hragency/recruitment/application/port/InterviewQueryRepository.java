package com.pl.hragency.recruitment.application.port;

import com.pl.hragency.recruitment.application.query.InterviewItem;
import com.pl.hragency.recruitment.application.query.InterviewListQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface InterviewQueryRepository {
    Page<InterviewItem> search(
            UUID organizationId,
            InterviewListQuery query,
            Pageable pageable
    );
}
