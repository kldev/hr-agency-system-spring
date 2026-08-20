package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.recruitment.application.port.JobPostingQueryRepository;
import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import com.pl.hragency.shared.rest.SliceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class GetJobPostingQueryHandler {
    private final JobPostingRepository repository;
    private final JobPostingQueryRepository queryRepository;
    private final JobPostingItemMapper mapper;
    public GetJobPostingQueryHandler(
            JobPostingRepository repository, JobPostingQueryRepository queryRepository, JobPostingItemMapper mapper
    ) {
        this.repository = repository;
        this.queryRepository = queryRepository;
        this.mapper = mapper;
    }

    public JobPostingItem get(
            UUID organizationId,
            JobPostingId jobPostingId
    ) {
        return repository
                .findById(organizationId, jobPostingId)
                .map(mapper::from)
                .orElseThrow(() ->
                        new EntityNotFoundException(EntityType.JobDescription, jobPostingId.value())
                );
    }

    public SliceResponse<JobPostingItem> search(
            UUID organizationId,
            JobPostingListQuery query,
            Pageable pageable
    ) {
        return queryRepository
                .search(organizationId, query, pageable);

    }

    public long countSearch(
            UUID organizationId,
            JobPostingListQuery query
    ) {
        return queryRepository.countSearch(organizationId,query);
    }
}
