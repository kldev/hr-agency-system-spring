package com.pl.hragency.testsupport;

import com.pl.hragency.company.application.command.CreateCompanyContactCommand;
import com.pl.hragency.company.application.handler.CreateCompanyContactHandler;

import com.pl.hragency.company.domain.model.CompanyContactCompanyId;
import com.pl.hragency.shared.rest.ExecutionContext;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

@Component
public class TestCompanyContactFactory {

    private final CreateCompanyContactHandler handler;
    private final Faker faker;

    public TestCompanyContactFactory(CreateCompanyContactHandler handler) {
        this.handler = handler;
        this.faker = new Faker(Locale.forLanguageTag("pl"));
    }

    public UUID createCompanyContact(UUID orgId, UUID companyId) {
        return createCompanyContact(orgId,
                companyId,
                faker.name().firstName(),
                faker.name().lastName(),
                faker.phoneNumber().cellPhone(),
                faker.siliconValley().email(),
                faker.job().position(), false);
    }

    public UUID createCompanyContact(UUID  orgId,
                                     UUID companyId,
                                     String firstName,
                                     String lastName,
                                     String phone,
                                     String email,
                                     String jobTitle, boolean isPrimary) {

        return handler.handle(new ExecutionContext(orgId, UUID.randomUUID(), "Test"),
                new CompanyContactCompanyId(companyId),
                new CreateCompanyContactCommand(email, phone, firstName, lastName, jobTitle, isPrimary )).value();
    }
}
