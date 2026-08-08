package com.pl.hragency.company;

import com.pl.hragency.BaseApiIntegrationTest;
import com.pl.hragency.company.application.query.CompanyDetailsItem;
import com.pl.hragency.company.application.query.CompanyListItem;
import com.pl.hragency.shared.rest.PageResponse;
import com.pl.hragency.testsupport.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyContactQueryTest extends BaseApiIntegrationTest {

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private TestCompanyContactFactory  companyContactFactory;


    @Test
    void shouldReturnCompanyDetailsWithContacts() {
        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization);

       var company = companyFactory.create(
                organization.id());

        IntStream.rangeClosed(1, 5)
                .forEach(i -> companyContactFactory.createCompanyContact(organization.id(), company));

        var token = authenticationClient.login(user);

        // when
        var response = restTestClient
                .get()
                .uri(url("/api/company/%s").formatted(company))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CompanyDetailsItem.class)
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.contacts())
                .hasSize(5);

    }

}
