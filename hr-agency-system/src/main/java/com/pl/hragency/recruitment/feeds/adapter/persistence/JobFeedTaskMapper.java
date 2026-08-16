package com.pl.hragency.recruitment.feeds.adapter.persistence;

import com.pl.hragency.recruitment.feeds.domain.model.JobFeedTask;
import org.springframework.stereotype.Component;

@Component
public class JobFeedTaskMapper {

    public JobFeedTask toDomain(JobFeedTaskJpaEntity entity) {
        return JobFeedTask.rehydrate(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getStatus(),
                entity.getAttempts(),
                entity.getCreatedAt(),
                entity.getStartedAt(),
                entity.getCompletedAt(),
                entity.getErrorMessage()
        );
    }

    public JobFeedTaskJpaEntity toEntity(JobFeedTask domain) {
        return new JobFeedTaskJpaEntity(
                domain.id(),
                domain.organizationId(),
                domain.status(),
                domain.attempts(),
                domain.createdAt(),
                domain.startedAt(),
                domain.completedAt(),
                domain.errorMessage());
    }
}
