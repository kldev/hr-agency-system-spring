package com.pl.hragency.recruitment.adapter.persistence.candidate;

import com.pl.hragency.recruitment.domain.model.candidate.Candidate;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import org.springframework.stereotype.Component;

@Component
public class CandidateMapper {

    public CandidateJpaEntity createNew(Candidate candidate) {
        return new CandidateJpaEntity(
                candidate.id().value(),
                candidate.organizationId(),
                candidate.email(),
                candidate.firstName(),
                candidate.lastName(),
                candidate.phone(),
                candidate.status(),
                candidate.source(),
                candidate.createdAt()
        );
    }

    public void updateExisting(Candidate candidate, CandidateJpaEntity entity) {
      entity.update(candidate.email(),
              candidate.firstName(),
              candidate.lastName(),
              candidate.phone(),
              candidate.status(),
              candidate.summary());
    }

    public Candidate toDomain(CandidateJpaEntity entity) {
        return Candidate.rehydrate(
                new CandidateId(entity.getId()),
                entity.getOrganizationId(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getSummary(),
                entity.getStatus(),
                entity.getSource(),
                entity.getCreatedAt()
        );
    }
}