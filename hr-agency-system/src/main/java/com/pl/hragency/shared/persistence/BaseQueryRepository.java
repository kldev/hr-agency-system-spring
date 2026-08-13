package com.pl.hragency.shared.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public abstract class BaseQueryRepository {

    protected final EntityManager entityManager;

    protected BaseQueryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected <T> List<T> execute(
            CriteriaQuery<T> query,
            Pageable pageable
    ) {
        TypedQuery<T> typedQuery = entityManager.createQuery(query);

        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        return typedQuery.getResultList();
    }

    protected <E, R> void applySpecification(
            Specification<E> specification,
            Root<E> root,
            CriteriaQuery<R> query,
            CriteriaBuilder cb
    ) {
        if (specification == null) {
            return;
        }

        Predicate predicate = specification.toPredicate(root, query, cb);

        if (predicate != null) {
            query.where(predicate);
        }
    }

    protected <E, R> void applySort(
            Pageable pageable,
            Root<E> root,
            CriteriaQuery<R> query,
            CriteriaBuilder cb
    ) {
        if (!pageable.getSort().isSorted()) {
            return;
        }

        var orders = pageable.getSort()
                .stream()
                .map(order -> order.isAscending()
                        ? cb.asc(root.get(order.getProperty()))
                        : cb.desc(root.get(order.getProperty())))
                .toList();

        query.orderBy(orders);
    }
}
