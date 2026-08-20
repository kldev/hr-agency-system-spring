package com.pl.hragency.recruitment.adapter.persistence.application;

import com.pl.hragency.recruitment.application.port.JobApplicationQueryRepository;
import com.pl.hragency.recruitment.application.query.JobApplicationItem;
import com.pl.hragency.recruitment.application.query.JobApplicationListQuery;
import com.pl.hragency.shared.persistence.AbstractJpaSliceQueryRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JobApplicationQueryRepositoryAdapter
        extends AbstractJpaSliceQueryRepository<JobApplicationReadJpaEntity,
                                                JobApplicationListQuery,
                                                JobApplicationItem>
        implements JobApplicationQueryRepository {

    public JobApplicationQueryRepositoryAdapter(EntityManager entityManager) {
       super(entityManager);
    }

    @Override protected Class<JobApplicationReadJpaEntity> entityType() {
        return JobApplicationReadJpaEntity.class;
    }


    @Override
    protected Specification<JobApplicationReadJpaEntity> specification(
            UUID organizationId,
            JobApplicationListQuery query) {

        return Specification.allOf(
                JobApplicationReadSpecifications.organizationId(organizationId),
                JobApplicationReadSpecifications.companyId(query.companyId()),
                JobApplicationReadSpecifications.recruiterId(query.recruiterId()),
                JobApplicationReadSpecifications.postingId(query.postingId()),
                JobApplicationReadSpecifications.search(query.search())
        );
    }

    @Override
    protected JobApplicationItem from(
            JobApplicationReadJpaEntity entity) {

        return new JobApplicationItem(
                entity.getId(),
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