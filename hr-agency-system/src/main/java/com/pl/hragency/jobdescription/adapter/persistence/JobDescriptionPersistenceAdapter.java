package com.pl.hragency.jobdescription.adapter.persistence;

import com.pl.hragency.jobdescription.application.port.JobDescriptionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.pl.hragency.jobdescription.application.query.JobDescriptionListQuery;
import com.pl.hragency.jobdescription.domain.model.JobDescription;
import com.pl.hragency.jobdescription.domain.model.JobDescriptionId;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class JobDescriptionPersistenceAdapter implements JobDescriptionRepository {
    private final SpringDataJobDescriptionRepository jobDescriptionRepository;
    private final JobDescriptionMapper mapper;

    public JobDescriptionPersistenceAdapter(SpringDataJobDescriptionRepository jobDescriptionRepository,
                                            JobDescriptionMapper mapper) {
        this.jobDescriptionRepository = jobDescriptionRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(JobDescription jobDescription) {

        jobDescriptionRepository.save(mapper.toEntity(jobDescription));
    }

    @Override
    public Optional<JobDescription> findById(UUID organizationId, JobDescriptionId id) {

        return jobDescriptionRepository
                .findByIdAndOrganizationId(id.value(), organizationId).map(mapper::toDomain);
    }

    @Override
    public List<JobDescription> findByCompanyId(UUID organizationId, UUID companyId) {
        var specification = Specification
                .allOf(
                        JobDescriptionSpecifications.organizationId(organizationId),
                        JobDescriptionSpecifications.companyId(companyId)
                );
        return jobDescriptionRepository
                .findAll(specification).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Page<JobDescription> search(UUID organizationId, JobDescriptionListQuery query) {
        var specification = Specification
                .allOf(
                        JobDescriptionSpecifications.organizationId(organizationId),
                        JobDescriptionSpecifications.search(query.search())
                );

        return jobDescriptionRepository
                .findAll(specification, query.pageable()).map(mapper::toDomain);
    }
}
