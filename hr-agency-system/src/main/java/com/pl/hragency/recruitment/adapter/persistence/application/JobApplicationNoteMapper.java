package com.pl.hragency.recruitment.adapter.persistence.application;

import com.pl.hragency.recruitment.domain.model.application.JobApplicationNote;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationNoteId;
import org.springframework.stereotype.Component;



@Component
public class JobApplicationNoteMapper {
    public JobApplicationNote toDomain(JobApplicationNoteJpaEntity entity) {
        return JobApplicationNote.rehydrate(
                new JobApplicationNoteId(entity.getId()),
                entity.getOrganizationId(),
                entity.getAuthorId(),
                entity.getApplicationId(),
                entity.getContent(),
                entity.getCreatedAt());
    }

    public JobApplicationNoteJpaEntity toEntity(JobApplicationNote domain) {
        return new JobApplicationNoteJpaEntity(
                domain.id().value(),
                domain.organizationId(),
                domain.applicationId(),
                domain.authorId(),
                domain.content(),
                domain.createdAt()
                );
    }
}
