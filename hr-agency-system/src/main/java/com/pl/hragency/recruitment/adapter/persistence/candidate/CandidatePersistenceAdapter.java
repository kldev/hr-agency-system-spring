package com.pl.hragency.recruitment.adapter.persistence.candidate;

import com.pl.hragency.recruitment.application.port.CandidateRepository;

import com.pl.hragency.recruitment.domain.model.candidate.Candidate;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateEmail;
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
    public void create(Candidate candidate) {
        var entity = mapper.createNew(candidate);

        this.repository.save(entity);

    }

    @Override
    public void update(Candidate candidate) {
        var entity = repository.findByIdAndOrganizationId(candidate.id().value(), candidate.organizationId())
                .orElseThrow();

        mapper.updateExisting(candidate, entity);

        this.repository.save(entity);
    }

    @Override
    public Optional<Candidate> findByEmail(CandidateEmail email, UUID organizationId) {
        return repository.findByEmailAndOrganizationId(email.value(), organizationId).map(mapper::toDomain);
    }

    @Override
    public Optional<Candidate> findById(UUID organizationId, CandidateId id) {

        return repository.findByIdAndOrganizationId(id.value(), organizationId).map(mapper::toDomain);
    }
}
