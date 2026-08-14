package com.pl.hragency.testsupport;

import com.pl.hragency.recruitment.application.command.CreateJobApplicationNoteCommand;
import com.pl.hragency.recruitment.application.handler.CreateJobApplicationNoteHandler;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationId;
import com.pl.hragency.shared.rest.ExecutionContext;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

@Component
public class TestJobApplicationNoteFactory {
    private final CreateJobApplicationNoteHandler handler;
    private final Faker faker;

    public TestJobApplicationNoteFactory(CreateJobApplicationNoteHandler handler) {
        this.handler = handler;
        faker = new Faker(Locale.forLanguageTag("pl"));
    }

    public UUID create(UUID organizationId, UUID userId, UUID applicationId){
        return create(organizationId, userId, applicationId, faker.lorem().sentence());
    }

    public UUID create(UUID organizationId, UUID userId, UUID applicationId, String content) {
        var context = new ExecutionContext(organizationId, userId, "Test");
        var command = new CreateJobApplicationNoteCommand(content);

        return handler.handle(context, new JobApplicationId(applicationId), command);
    }
}
