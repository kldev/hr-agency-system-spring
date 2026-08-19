package com.pl.hragency.recruitment.adapter.persistence.application;

import com.pl.hragency.recruitment.application.port.JobApplicationQueryRepository;
import com.pl.hragency.recruitment.application.query.JobApplicationItem;
import com.pl.hragency.recruitment.application.query.JobApplicationListQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JobApplicationQueryRepositoryImpl
        implements JobApplicationQueryRepository {

    private final EntityManager entityManager;

    public JobApplicationQueryRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Slice<JobApplicationItem> search(
            UUID organizationId,
            JobApplicationListQuery query,
            Pageable pageable) {

        var specification = specification(organizationId, query);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<JobApplicationReadJpaEntity> cq =
                cb.createQuery(JobApplicationReadJpaEntity.class);

        Root<JobApplicationReadJpaEntity> root =
                cq.from(JobApplicationReadJpaEntity.class);

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
                .setMaxResults(pageable.getPageSize() + 1)
                .getResultList();

        boolean hasNext = result.size() > pageable.getPageSize();

        if (hasNext) {
            result = result.subList(0, pageable.getPageSize());
        }

        List<JobApplicationItem> content = result.stream()
                .map(JobApplicationQueryRepositoryImpl::from)
                .toList();

        return new SliceImpl<>(
                content,
                pageable,
                hasNext
        );
    }

    @Override
    public long countSearch(
            UUID organizationId,
            JobApplicationListQuery query,
            Pageable pageable) {

        var specification = specification(organizationId, query);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<JobApplicationReadJpaEntity> root =
                cq.from(JobApplicationReadJpaEntity.class);

        cq.select(cb.count(root));

        var predicate = specification.toPredicate(root, cq, cb);

        if (predicate != null) {
            cq.where(predicate);
        }

        return entityManager
                .createQuery(cq)
                .getSingleResult();
    }

    private Specification<JobApplicationReadJpaEntity> specification(
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

    public static JobApplicationItem from(
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