package com.pl.hragency.recruitment.adapter.persistence.application;

import com.pl.hragency.recruitment.application.port.JobApplicationRepository;
import com.pl.hragency.recruitment.domain.model.application.JobApplication;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationId;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;

import java.util.Optional;
import java.util.UUID;

import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import org.springframework.stereotype.Component;

@Component
public class JobApplicationPersistenceAdapter implements JobApplicationRepository {
    private final SpringDataJobApplicationRepository repository;
    private final JobApplicationMapper mapper;

    public JobApplicationPersistenceAdapter(SpringDataJobApplicationRepository repository, JobApplicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(JobApplication jobApplication) {
        repository.save(mapper.toEntity(jobApplication));
    }

    @Override
    public Optional<JobApplication> findByCandidate(CandidateId candidateId, UUID organizationId, JobPostingId postingId) {

        return repository.findByCandidateIdAndJobPostingIdAndOrganizationId(candidateId.value(), postingId.value(), organizationId).map(mapper::toDomain);
    }

    @Override
    public Optional<JobApplication> findById(JobApplicationId id, UUID organizationId) {
        return repository.findByIdAndOrganizationId(id.value(), organizationId).map(mapper::toDomain);
    }
}
