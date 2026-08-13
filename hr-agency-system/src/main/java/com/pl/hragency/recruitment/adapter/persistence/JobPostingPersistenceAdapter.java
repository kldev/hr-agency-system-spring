package com.pl.hragency.recruitment.adapter.persistence;


import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.application.query.JobPostingListQuery;
import com.pl.hragency.recruitment.domain.model.posting.JobPosting;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.Instant;
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
    public void save(JobPosting jobPosting) {
        repository.save(mapper.fromDomain(jobPosting));
    }

    @Override
    public Optional<JobPosting> findById(UUID organizationId, JobPostingId id) {
        return repository.findByIdAndOrganizationId(id.value(), organizationId).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(UUID organizationId, JobPostingId id) {
         return repository.existsByIdAndOrganizationId(id.value(), organizationId);
    }

    @Override
    public int updateStatus(UUID organizationId, JobPostingId id, JobPostingStatus newStatus, Instant modifiedAt) {
        return repository.updateStatus(id.value(), organizationId, newStatus, modifiedAt);
    }

    @Override
    public int updateRecruiter(UUID organizationId, JobPostingId id, UUID recruiterId) {
        return repository.updateRecruiter(id.value(), organizationId, recruiterId, Instant.now());
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
