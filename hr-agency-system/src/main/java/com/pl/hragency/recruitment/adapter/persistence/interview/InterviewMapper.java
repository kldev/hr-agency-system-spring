package com.pl.hragency.recruitment.adapter.persistence.interview;
import com.pl.hragency.recruitment.domain.model.interview.Interview;
import com.pl.hragency.recruitment.domain.model.interview.InterviewId;
import org.springframework.stereotype.Component;

@Component
public class InterviewMapper {

    public Interview toDomain(InterviewJpaEntity entity) {
        return Interview.rehydrate(new InterviewId(entity.getId()),
                entity.getOrganizationId(),
                entity.getCandidateId(),
                entity.getApplicationId(),
                entity.getStatus(),
                entity.getScheduledAt(),
                entity.getFeedback(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getCreatedBy()
                );
    }
    public InterviewJpaEntity toEntity(Interview interview) {
        return new InterviewJpaEntity(
                interview.id().value(),
                interview.organizationId(),
                interview.candidateId(),
                interview.applicationId(),
                interview.feedback(),
                interview.status(),
                interview.scheduledAt(),
                interview.createdAt(),
                interview.updatedAt(),
                interview.createdBy()
        );
    }
}
