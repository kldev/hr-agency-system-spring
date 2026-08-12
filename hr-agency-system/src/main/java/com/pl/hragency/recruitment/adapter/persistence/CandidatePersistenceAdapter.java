package com.pl.hragency.recruitment.adapter.persistence;

import com.pl.hragency.recruitment.application.port.CandidateRepository;

import com.pl.hragency.recruitment.domain.model.candidate.Candidate;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CandidatePersistenceAdapter implements CandidateRepository {
    private final SpringDataCandidateRepository repository;
    private final CandidateMapper mapper;

    public CandidatePersistenceAdapter(SpringDataCandidateRepository repository, CandidateMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(Candidate candidate) {

        repository.save(mapper.toEntity(candidate));
    }

    @Override
    public boolean existsByEmail(String email, UUID organizationId) {
        return repository.existsByEmailAndOrganizationId(normalizeEmail(email), organizationId);
    }

    @Override
    public Optional<Candidate> findById(UUID organizationId, CandidateId id) {

        return repository.findByIdAndOrganizationId(id.value(), organizationId).map(mapper::toDomain);
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
