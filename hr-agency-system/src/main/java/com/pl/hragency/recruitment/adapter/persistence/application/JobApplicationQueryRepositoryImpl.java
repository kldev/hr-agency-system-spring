package com.pl.hragency.recruitment.adapter.persistence.application;

import com.pl.hragency.recruitment.application.port.JobApplicationQueryRepository;

import com.pl.hragency.recruitment.application.query.JobApplicationItem;
import com.pl.hragency.recruitment.application.query.JobApplicationListQuery;
import com.pl.hragency.shared.rest.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component

public class JobApplicationQueryRepositoryImpl implements JobApplicationQueryRepository {
    private final SpringDataJobApplicationReadRepository repository;

    public JobApplicationQueryRepositoryImpl(SpringDataJobApplicationReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResponse<JobApplicationItem> search(UUID organizationId, JobApplicationListQuery query, Pageable pageable) {
        var specification = Specification.allOf(
                JobApplicationReadSpecifications.organizationId(organizationId),
                JobApplicationReadSpecifications.companyId(query.companyId()),
                JobApplicationReadSpecifications.recruiterId(query.recruiterId()),
                JobApplicationReadSpecifications.postingId(query.postingId()),
                JobApplicationReadSpecifications.search(query.search())
        );

        return PageResponse.from(repository.findAll(specification, pageable)
                .map(JobApplicationQueryRepositoryImpl::from));
    }

    public static JobApplicationItem from(JobApplicationReadJpaEntity entity) {
        return new JobApplicationItem(entity.getId(),
                entity.getCandidateId(),
                entity.getCandidateEmail(),
                entity.getCandidateFirstName(),
                entity.getCandidateLastName(),
                entity.getCandidatePhone(),
                entity.getSource(),
                entity.getCreatedAt(),
                entity.getRecruiterId(),
                entity.getRecruiterFullName(),
                entity.getCompanyId()
                );
    }
}
