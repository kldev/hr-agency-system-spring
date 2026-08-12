package com.pl.hragency.recruitment.adapter.persistence;

import com.pl.hragency.recruitment.domain.model.candidate.Candidate;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import org.springframework.stereotype.Component;

@Component
public class CandidateMapper {

    public CandidateJpaEntity toEntity(Candidate candidate) {
        return new CandidateJpaEntity(
                candidate.id().value(),
                candidate.organizationId(),
                candidate.email(),
                candidate.firstName(),
                candidate.lastName(),
                candidate.phone(),
                candidate.status(),
                candidate.source(),
                candidate.createdAt(),
                candidate.updatedAt()
        );
    }

    public Candidate toDomain(CandidateJpaEntity entity) {
        return Candidate.rehydrate(
                new CandidateId(entity.getId()),
                entity.getOrganizationId(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getStatus(),
                entity.getSource(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}