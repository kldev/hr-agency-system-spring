package com.pl.hragency.company.adapter.persistence;

import com.pl.hragency.shared.persistence.BaseQueryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CompanyQueryRepositoryImpl extends BaseQueryRepository implements CompanyQueryRepository{


    public CompanyQueryRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<CompanySuggestionProjection> findSuggestions(Specification<CompanyJpaEntity> specification, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<CompanySuggestionProjection> query =
                cb.createQuery(CompanySuggestionProjection.class);

        Root<CompanyJpaEntity> root = query.from(CompanyJpaEntity.class);

        query.select(cb.construct(
                CompanySuggestionProjection.class,
                root.get("id"),
                root.get("name"),
                root.get("taxId"),
                root.get("countryCode")
        ));

        applySpecification(specification, root, query, cb);
        applySort(pageable, root, query, cb);

        return execute(query, pageable);
    }
}
