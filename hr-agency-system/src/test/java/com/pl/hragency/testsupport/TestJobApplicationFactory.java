package com.pl.hragency.testsupport;

import com.pl.hragency.recruitment.application.command.CreateJobApplicationCommand;
import com.pl.hragency.recruitment.application.handler.CreateJobApplicationHandler;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import com.pl.hragency.shared.rest.ExecutionContext;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

@Component
public class TestJobApplicationFactory {
    private final CreateJobApplicationHandler handler;
    private final Faker faker;
    public TestJobApplicationFactory(CreateJobApplicationHandler handler) {
        this.handler = handler;
        this.faker = new Faker(Locale.forLanguageTag("pl"));
    }

    public UUID create(UUID organizationId, UUID userId, UUID jobPostingId)
    {
        return create(organizationId, userId, jobPostingId, faker.internet().emailAddress(), CandidateSource.JUST_JOIN_IT);
    }

    public UUID create(UUID organizationId, UUID userId, UUID jobPostingId, String email, CandidateSource source) {
        var context = new ExecutionContext(organizationId, userId, "Test");
        var command = new CreateJobApplicationCommand(jobPostingId, email, "", "", "", source);
        return handler.handle(context, command).applicationId();
    }
}
