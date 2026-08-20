package com.pl.hragency.recruitment.adapter.persistence.candidate;

import com.pl.hragency.recruitment.application.port.CandidateQueryRepository;
import com.pl.hragency.recruitment.application.query.CandidateItem;
import com.pl.hragency.recruitment.application.query.CandidateListQuery;
import com.pl.hragency.shared.persistence.AbstractJpaSliceQueryRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class CandidateQueryRepositoryAdapter
        extends AbstractJpaSliceQueryRepository<CandidateJpaEntity,
                                                CandidateListQuery,
                                                CandidateItem>
        implements CandidateQueryRepository  {

    public CandidateQueryRepositoryAdapter(EntityManager entityManager) {
        super(entityManager);
    }

    @Override protected Class<CandidateJpaEntity> entityType() {
        return CandidateJpaEntity.class;
    }

    @Override
    protected Specification<CandidateJpaEntity> specification(UUID organizationId, CandidateListQuery query) {
        return Specification.allOf(
                CandidateSpecifications.organizationId(organizationId),
                CandidateSpecifications.search(query.search()),
                CandidateSpecifications.appliedToCompany(query.companyId()),
                CandidateSpecifications.tags(query.tags()),
                CandidateSpecifications.status(query.status())
        );

    }

    @Override
    protected CandidateItem from(CandidateJpaEntity entity) {
        return new CandidateItem(entity.getId(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getSummary(),
                entity.getPhone(), entity.getCreatedAt());
    }
}
