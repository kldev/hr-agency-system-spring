package com.pl.hragency.recruitment.adapter.persistence.interview;

import com.pl.hragency.identity.adapter.persistence.UserJpaEntity;
import com.pl.hragency.recruitment.adapter.persistence.candidate.CandidateJpaEntity;
import com.pl.hragency.recruitment.application.port.InterviewQueryRepository;
import com.pl.hragency.recruitment.application.query.InterviewItem;
import com.pl.hragency.recruitment.application.query.InterviewListQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class InterviewQueryRepositoryImpl implements InterviewQueryRepository {

    private final EntityManager entityManager;

    public InterviewQueryRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<InterviewItem> search(
            UUID organizationId,
            InterviewListQuery query,
            Pageable pageable
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<InterviewItem> criteriaQuery =
                cb.createQuery(InterviewItem.class);

        Root<InterviewJpaEntity> interview =
                criteriaQuery.from(InterviewJpaEntity.class);

        Root<CandidateJpaEntity> candidate =
                criteriaQuery.from(CandidateJpaEntity.class);

        Root<UserJpaEntity> user =
                criteriaQuery.from(UserJpaEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(
                cb.equal(
                        interview.get("organizationId"),
                        organizationId
                )
        );

        predicates.add(
                cb.equal(
                        candidate.get("id"),
                        interview.get("candidateId")
                )
        );

        predicates.add(
                cb.equal(
                        user.get("id"),
                        interview.get("createdBy")
                )
        );

        if (query.createdBy() != null) {
            predicates.add(
                    cb.equal(
                            interview.get("createdBy"),
                            query.createdBy()
                    )
            );
        }

        Instant from = query.fromAtInstant();

        if (from != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(
                            interview.get("scheduledAt"),
                            from
                    )
            );
        }

        Instant to = query.toAtInstant();

        if (to != null) {
            predicates.add(
                    cb.lessThan(
                            interview.get("scheduledAt"),
                            to
                    )
            );
        }

        criteriaQuery.select(
                cb.construct(
                        InterviewItem.class,
                        interview.get("id"),
                        interview.get("candidateId"),
                        cb.concat(
                                cb.concat(
                                        candidate.get("firstName"),
                                        " "
                                ),
                                candidate.get("lastName")
                        ),
                        candidate.get("email"),
                        interview.get("applicationId"),
                        interview.get("status"),
                        interview.get("feedback"),
                        interview.get("scheduledAt"),
                        interview.get("createdAt"),
                        interview.get("createdBy"),
                        cb.concat(
                                cb.concat(
                                        user.get("firstName"),
                                        " "
                                ),
                                user.get("lastName")
                        )
                )
        );

        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        criteriaQuery.orderBy(
                cb.asc(interview.get("scheduledAt"))
        );

        TypedQuery<InterviewItem> typedQuery =
                entityManager.createQuery(criteriaQuery);

        typedQuery.setFirstResult(
                (int) pageable.getOffset()
        );

        typedQuery.setMaxResults(
                pageable.getPageSize()
        );

        List<InterviewItem> items = typedQuery.getResultList();

        long total = count(
                organizationId,
                query
        );

        return new PageImpl<>(
                items,
                pageable,
                total
        );
    }

    private long count(
            UUID organizationId,
            InterviewListQuery query
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery =
                cb.createQuery(Long.class);

        Root<InterviewJpaEntity> interview =
                countQuery.from(InterviewJpaEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(
                cb.equal(
                        interview.get("organizationId"),
                        organizationId
                )
        );

        if (query.createdBy() != null) {
            predicates.add(
                    cb.equal(
                            interview.get("createdBy"),
                            query.createdBy()
                    )
            );
        }

        Instant from = query.fromAtInstant();

        if (from != null) {
            predicates.add(
                    cb.greaterThanOrEqualTo(
                            interview.get("scheduledAt"),
                            from
                    )
            );
        }

        Instant to = query.toAtInstant();

        if (to != null) {
            predicates.add(
                    cb.lessThan(
                            interview.get("scheduledAt"),
                            to
                    )
            );
        }

        countQuery.select(
                cb.count(interview)
        );

        countQuery.where(
                predicates.toArray(Predicate[]::new)
        );

        return entityManager
                .createQuery(countQuery)
                .getSingleResult();
    }
}