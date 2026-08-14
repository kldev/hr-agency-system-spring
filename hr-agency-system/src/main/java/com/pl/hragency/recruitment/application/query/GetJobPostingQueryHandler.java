package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class GetJobPostingQueryHandler {
    private final JobPostingRepository repository;

    public GetJobPostingQueryHandler(
            JobPostingRepository repository
    ) {
        this.repository = repository;
    }

    public JobPostingItem get(
            UUID organizationId,
            JobPostingId jobPostingId
    ) {
        return repository
                .findById(organizationId, jobPostingId)
                .map(JobPostingItem::from)
                .orElseThrow(() ->
                        new EntityNotFoundException(EntityType.JobDescription, jobPostingId.value())
                );
    }

    public Page<JobPostingItem> search(
            UUID organizationId,
            JobPostingListQuery query,
            Pageable pageable
    ) {
        return repository
                .search(organizationId, query, pageable).map(JobPostingItem::from);

    }
}
