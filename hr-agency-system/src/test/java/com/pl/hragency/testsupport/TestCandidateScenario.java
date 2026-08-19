package com.pl.hragency.testsupport;
import com.pl.hragency.recruitment.application.port.CandidateRepository;
import com.pl.hragency.recruitment.domain.model.candidate.Candidate;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class TestCandidateScenario {

    private final TestOrganizationScenario organizationScenario;
    private final TestCandidateFactory candidateFactory;
    private final CandidateRepository candidateRepository;

    public TestCandidateScenario(
            TestOrganizationScenario organizationScenario,
            TestCandidateFactory candidateFactory, CandidateRepository candidateRepository
    ) {
        this.organizationScenario = organizationScenario;
        this.candidateFactory = candidateFactory;
        this.candidateRepository = candidateRepository;
    }

    public Scenario create(String email) {
        var organization = organizationScenario.create();

        var candidate = candidateFactory.create(
                organization.organization().id(),
                email
        );

        return new Scenario(
                organization.organization(),
                organization.recruiter(),
                candidate
        );
    }
    public Scenario createCandidate( String firstName, String lastName, String email, String phoneNumber) {
        var organization = organizationScenario.create();

        var candidate = candidateFactory.create(
                organization.organization().id(),
                firstName, lastName,
                email, phoneNumber);

        return new Scenario(
                organization.organization(),
                organization.recruiter(),
                candidate
        );
    }

    public Scenario createCandidate(TestOrganization organization, TestUser recruiter, String firstName, String lastName, String email, String phoneNumber) {
        var candidate = candidateFactory.create(
                organization.id(),
                firstName, lastName,
                email, phoneNumber);

        return new Scenario(
                organization,
                recruiter,
                candidate
        );
    }

    @Transactional
    public void changeStatus(UUID organizationId, UUID candidateId, CandidateStatus status) {
        Candidate exists = candidateRepository.findById(organizationId, new CandidateId(candidateId)).orElseThrow();

        exists.updateStatus(status);
        candidateRepository.update(exists);
    }

    public record Scenario(
            TestOrganization organization,
            TestUser recruiter,
            TestCandidate candidate
    ) {
        public UUID id() {
            return candidate.id();
        }

        public UUID organizationId(){
            return organization.id();
        }
    }
}