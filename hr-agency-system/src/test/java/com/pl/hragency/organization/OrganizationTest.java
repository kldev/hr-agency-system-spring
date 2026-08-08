package com.pl.hragency.organization;

import com.pl.hragency.organization.domain.model.Organization;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrganizationTest {

    @Test
    void shouldCreateOrganization() {

        var organization =
                Organization.create(
                        "Acme",
                        "acme"
                );

        assertThat(organization.name())
                .isEqualTo("Acme");

        assertThat(organization.slug())
                .isEqualTo("acme");

        assertThat(organization.id())
                .isNotNull();
    }

    @Test
    void shouldRejectBlankName() {

        assertThatThrownBy(() ->
                Organization.create("", "acme"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
