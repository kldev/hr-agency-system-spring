package com.pl.hragency.recruitment.adapter.persistence;

import com.pl.hragency.recruitment.domain.model.application.JobApplication;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationId;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import org.springframework.stereotype.Component;

@Component
public class JobApplicationMapper {

    public JobApplicationJpaEntity toEntity(JobApplication application) {
        return new JobApplicationJpaEntity(
                application.id().value(),
                application.organizationId(),
                application.jobPostingId().value(),
                application.candidateId().value(),
                application.status(),
                application.source(),
                application.createdAt(),
                application.updatedAt()
        );
    }

    public JobApplication toDomain(JobApplicationJpaEntity entity) {
        return JobApplication.rehydrate(
                new JobApplicationId(entity.getId()),
                entity.getOrganizationId(),
                new JobPostingId(entity.getJobPostingId()),
                new CandidateId(entity.getCandidateId()),
                entity.getStatus(),
                entity.getSource(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
