package com.pl.hragency.recruitment.adapter.persistence.posting;


import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.application.query.JobPostingListQuery;
import com.pl.hragency.recruitment.domain.model.posting.JobPosting;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class JobPostingPersistenceAdapter implements JobPostingRepository {

    private final SpringDataJobPostingRepository repository;
    private final JobPostingMapper mapper;

    public JobPostingPersistenceAdapter(SpringDataJobPostingRepository jobPostingRepository, JobPostingMapper jobPostingMapper) {
        this.repository = jobPostingRepository;
        this.mapper = jobPostingMapper;
    }

    @Override
    public void create(JobPosting jobPosting) {
        var entity = mapper.createNew(jobPosting);

        repository.save(entity);
    }

    @Override
    public void update(JobPosting jobPosting) {
        var entity = repository.findByIdAndOrganizationId(jobPosting.id().value(), jobPosting.organizationId())
                .orElseThrow();
        mapper.updateExisting(jobPosting, entity);
        repository.save(entity);
    }

    @Override
    public Optional<JobPosting> findById(UUID organizationId, JobPostingId id) {
        return repository.findByIdAndOrganizationId(id.value(), organizationId).map(mapper::toDomain);
    }

    @Override
    public Optional<JobPosting> findBySlug(UUID organizationId, String slug) {
        return repository.findBySlugAndOrganizationId(slug, organizationId).map(mapper::toDomain);
    }

    @Override
    public Page<JobPosting> search(UUID organizationId, JobPostingListQuery query, Pageable pageable) {
        Specification<JobPostingJpaEntity> specification = Specification.allOf(
                JobPostingSpecifications.organizationId(organizationId),
                JobPostingSpecifications.search(query.search()),
                JobPostingSpecifications.status(query.status()),
                JobPostingSpecifications.companyId(query.companyId()),
                JobPostingSpecifications.jobDescriptionId(query.jobDescriptionId())
        );
        return repository.findAll(specification, pageable).map(mapper::toDomain);
    }
}
