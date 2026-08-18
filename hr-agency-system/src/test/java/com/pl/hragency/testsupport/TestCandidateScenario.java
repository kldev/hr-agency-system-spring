package com.pl.hragency.testsupport;
import org.springframework.stereotype.Component;

@Component
public class TestCandidateScenario {

    private final TestOrganizationScenario organizationScenario;
    private final TestCandidateFactory candidateFactory;

    public TestCandidateScenario(
            TestOrganizationScenario organizationScenario,
            TestCandidateFactory candidateFactory
    ) {
        this.organizationScenario = organizationScenario;
        this.candidateFactory = candidateFactory;
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

    public record Scenario(
            TestOrganization organization,
            TestUser recruiter,
            TestCandidate candidate
    ) {
    }
}