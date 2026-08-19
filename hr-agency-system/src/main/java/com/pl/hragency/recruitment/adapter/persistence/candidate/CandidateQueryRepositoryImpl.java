package com.pl.hragency.recruitment.adapter.persistence.candidate;

import com.pl.hragency.recruitment.application.port.CandidateQueryRepository;
import com.pl.hragency.recruitment.application.query.CandidateItem;
import com.pl.hragency.recruitment.application.query.CandidateListQuery;
import com.pl.hragency.shared.rest.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class CandidateQueryRepositoryImpl implements CandidateQueryRepository {
    private final SpringDataCandidateRepository repository;

    public CandidateQueryRepositoryImpl(SpringDataCandidateRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResponse<CandidateItem> search(UUID organizationId, CandidateListQuery query, Pageable pageable) {

        var specification = Specification.allOf(
                CandidateSpecifications.organizationId(organizationId),
                CandidateSpecifications.search(query.search()),
                CandidateSpecifications.appliedToCompany(query.companyId()),
                CandidateSpecifications.tags(query.tags()),
                CandidateSpecifications.status(query.status())
        );

        return PageResponse.from(repository.findAll(specification, pageable).map(CandidateQueryRepositoryImpl::from));
    }

    public static CandidateItem from(CandidateJpaEntity entity) {
        return new CandidateItem(entity.getId(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(), entity.getSummary(), entity.getPhone(), entity.getCreatedAt());
    }
}
