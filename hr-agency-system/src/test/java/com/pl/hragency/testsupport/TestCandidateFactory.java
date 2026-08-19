package com.pl.hragency.testsupport;

import com.pl.hragency.recruitment.application.port.CandidateRepository;
import com.pl.hragency.recruitment.domain.model.candidate.Candidate;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class TestCandidateFactory {
    private final CandidateRepository repository;

    public TestCandidateFactory(CandidateRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TestCandidate create(UUID organizationId, String email) {
        Candidate candidate = Candidate.create(organizationId, email, "", "", "",
                CandidateSource.CAREER_PAGE);
        repository.create(candidate);

        return new TestCandidate(candidate.id().value(), email);
    }

    @Transactional
    public TestCandidate create(UUID organizationId, String firstName, String lastName, String email, String phone) {
        Candidate candidate = Candidate.create(organizationId, email, firstName, lastName, phone,
                CandidateSource.CAREER_PAGE);
        repository.create(candidate);

        return new TestCandidate(candidate.id().value(), email);
    }
}
