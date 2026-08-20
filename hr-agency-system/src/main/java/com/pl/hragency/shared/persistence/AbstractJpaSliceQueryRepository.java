package com.pl.hragency.shared.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public abstract class AbstractJpaSliceQueryRepository<
        E,
        Q,
        I> {

    protected final EntityManager entityManager;

    protected AbstractJpaSliceQueryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Slice<I> search(
            UUID organizationId,
            Q query,
            Pageable pageable) {

        Specification<E> specification =
                specification(organizationId, query);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<E> cq = cb.createQuery(entityType());

        Root<E> root = cq.from(entityType());

        cq.select(root);

        var predicate = specification.toPredicate(root, cq, cb);

        if (predicate != null) {
            cq.where(predicate);
        }

        if (pageable.getSort().isSorted()) {
            cq.orderBy(
                    pageable.getSort()
                            .stream()
                            .map(order -> order.isAscending()
                                    ? cb.asc(root.get(order.getProperty()))
                                    : cb.desc(root.get(order.getProperty())))
                            .toList()
            );
        }

        var result = entityManager
                .createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(Math.min(pageable.getPageSize() + 1, 501))
                .getResultList();

        boolean hasNext = result.size() > pageable.getPageSize();

        if (hasNext) {
            result = result.subList(0, pageable.getPageSize());
        }

        return new SliceImpl<>(
                result.stream()
                        .map(this::from)
                        .toList(),
                pageable,
                hasNext
        );
    }

    public long countSearch(
            UUID organizationId,
            Q query) {

        Specification<E> specification =
                specification(organizationId, query);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<E> root = cq.from(entityType());

        cq.select(cb.count(root));

        var predicate = specification.toPredicate(root, cq, cb);

        if (predicate != null) {
            cq.where(predicate);
        }

        return entityManager
                .createQuery(cq)
                .getSingleResult();
    }

    protected abstract Class<E> entityType();

    protected abstract Specification<E> specification(
            UUID organizationId,
            Q query);

    protected abstract I from(E entity);
}
