package com.pl.hragency.testsupport;

import com.pl.hragency.company.api.CompanyApi;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class TestCompanyFactory {

    private final CompanyApi companyApi;
    private final Faker faker;

    public TestCompanyFactory(CompanyApi companyApi) {
        this.companyApi = companyApi;
        this.faker = new Faker();
    }

    public UUID create(UUID organizationId) {
        return create(
                organizationId,
                faker.company().name(),
                faker.bothify("PL##########"),
                "PL",
                faker.bothify("REG-#####"),
                faker.address().city(),
                faker.address().streetAddress(),
                faker.address().postcode()
        );
    }

    public UUID create(
            UUID organizationId,
            String name,
            String taxId,
            String countryCode,
            String registrationNumber,
            String city,
            String address,
            String postalCode) {

        return companyApi.create(
                organizationId,
                name,
                countryCode,
                taxId,
                registrationNumber,
                city,
                address,
                postalCode
        );
    }
}
