package com.pl.hragency.development;

import com.pl.hragency.organization.api.OrganizationApi;
import com.pl.hragency.organization.application.port.OrganizationRepository;
import net.datafaker.Faker;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Profile("dev")
@Component
public class DevelopmentDataSeeder {
    private final DevelopmentScenario scenario;
    private final OrganizationApi organizationApi;

    public DevelopmentDataSeeder(
            DevelopmentScenario scenario,
            OrganizationApi organizationRepository) {

        this.scenario = scenario;
        this.organizationApi = organizationRepository;

    }

    @EventListener(ApplicationReadyEvent.class)
    public void seed() {

        if (organizationApi.existsBySlug(
                DevelopmentIds.ORGANIZATION_SLUG)) {
            return;
        }


        scenario.create();
    }
}
