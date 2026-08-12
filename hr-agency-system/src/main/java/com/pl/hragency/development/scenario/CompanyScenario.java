package com.pl.hragency.development.scenario;

import com.pl.hragency.company.api.CompanyApi;
import com.pl.hragency.identity.api.IdentityApi;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

@Component
public class CompanyScenario {
    private static final int COMPANY_COUNT = 50;

    private final SecureRandom secureRandom;
    private final CompanyApi api;
    private final Faker faker;
    private final IdentityApi identityApi;

    public CompanyScenario(
            CompanyApi api, IdentityApi identityApi) {

        this.api = api;
        this.identityApi = identityApi;
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
        var userId = identityApi.findUserSuggestions(orgId, "", Set.of("SALES")).getFirst().id();

       var companyId = api.create(userId, orgId,
                companyName,
               "PL",
                generateTaxNumber(),
                generateRegistrationNumber(),
                faker.address().city(),
                faker.address().streetAddress(),
                faker.address().zipCode()
        );

        IntStream.rangeClosed(1, secureRandom.nextInt(10) )
                .forEach(i -> api.createContact(userId, orgId, companyId,
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
