package com.pl.hragency.identity.adapter.persistence;

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
public class UserQueryRepositoryImpl extends BaseQueryRepository implements UserQueryRepository {

    public UserQueryRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<UserSuggestionProjection> suggestions(Specification<UserJpaEntity> specification, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<UserSuggestionProjection> query =
                cb.createQuery(UserSuggestionProjection.class);

        Root<UserJpaEntity> root = query.from(UserJpaEntity.class);

        query.select(cb.construct(
                UserSuggestionProjection.class,
                root.get("id"),
                root.get("firstName"),
                root.get("lastName"),
                root.get("email")
        ));

        applySpecification(specification, root, query, cb);
        applySort(pageable, root, query, cb);
        return  execute(query, pageable);
    }
}
