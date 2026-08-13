package com.pl.hragency.recruitment.application.port;

import com.pl.hragency.recruitment.domain.model.candidate.Candidate;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateEmail;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;


import java.util.Optional;
import java.util.UUID;

public interface CandidateRepository {
    void save(Candidate candidate);

    boolean existsByEmail(CandidateEmail email, UUID organizationId);
    Optional<Candidate> findByEmail(CandidateEmail email, UUID organizationId);
    Optional<Candidate> findById(
            UUID organizationId,
            CandidateId id);
}
