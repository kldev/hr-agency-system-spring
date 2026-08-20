package com.pl.hragency.recruitment.adapter.persistence.interview;

import com.pl.hragency.recruitment.adapter.persistence.application.JobApplicationReadJpaEntity;
import com.pl.hragency.recruitment.application.port.InterviewQueryRepository;
import com.pl.hragency.recruitment.application.query.InterviewItem;
import com.pl.hragency.recruitment.application.query.InterviewListQuery;
import com.pl.hragency.shared.persistence.AbstractJpaSliceQueryRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class InterviewQueryRepositoryAdapter
         extends AbstractJpaSliceQueryRepository<InterviewReadJpaEntity,
                 InterviewListQuery,
                 InterviewItem>
        implements InterviewQueryRepository {


    public InterviewQueryRepositoryAdapter(EntityManager entityManager) {
       super(entityManager);
    }

    @Override
    protected Class<InterviewReadJpaEntity> entityType() {
        return InterviewReadJpaEntity.class;
    }

    @Override
    protected Specification<InterviewReadJpaEntity> specification(UUID organizationId, InterviewListQuery query) {
        return Specification.allOf(
                InterviewReadSpecifications.organizationId(organizationId),
                InterviewReadSpecifications.from(query.fromAtInstant()),
                InterviewReadSpecifications.to(query.toAtInstant()),
                InterviewReadSpecifications.search(query.search()),
                InterviewReadSpecifications.createdBy(query.createdBy())
        );
    }

    @Override
    protected InterviewItem from(InterviewReadJpaEntity entity) {
        return null;
    }


}