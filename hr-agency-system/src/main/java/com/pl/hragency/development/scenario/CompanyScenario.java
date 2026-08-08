package com.pl.hragency.development.scenario;

import com.pl.hragency.company.api.CompanyApi;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.IntStream;

@Component
public class CompanyScenario {
    private static final int COMPANY_COUNT = 50;

    private final SecureRandom secureRandom;
    private final CompanyApi api;
    private final Faker faker;

    public CompanyScenario(
            CompanyApi api) {

        this.api = api;
        this.faker = new Faker(Locale.forLanguageTag("pl"));
        this.secureRandom = new SecureRandom();
    }

    public void createCompanies(UUID orgId) {

        IntStream.rangeClosed(1, COMPANY_COUNT)
                .forEach(i -> createCompany(orgId));
    }

    private void createCompany(UUID orgId) {

        var companyName =
                faker.company().name();

       var companyId = api.create(orgId,
                companyName,
               "PL",
                generateTaxNumber(),
                generateRegistrationNumber(),
                faker.address().city(),
                faker.address().streetAddress(),
                faker.address().zipCode()
        );

        IntStream.rangeClosed(1, secureRandom.nextInt(10) )
                .forEach(i -> api.createContact(orgId, companyId,
                        faker.name().firstName(),
                        faker.name().lastName(),
                        faker.phoneNumber().cellPhone(),
                        faker.siliconValley().email(),
                        faker.job().position()));

    }

    private String generateTaxNumber() {
        return faker.number().digits(10);
    }

    private String generateRegistrationNumber() {
        return faker.number().digits(9);
    }
}
