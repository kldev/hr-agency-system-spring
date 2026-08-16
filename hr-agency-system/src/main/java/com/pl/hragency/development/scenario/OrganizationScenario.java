package com.pl.hragency.development.scenario;
import com.pl.hragency.organization.api.OrganizationApi;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrganizationScenario {
    private final OrganizationApi api;

    public OrganizationScenario(OrganizationApi api) {
        this.api = api;
    }

    public OrganizationResult create(String organizationName, String slug) {
        UUID uuid = api.create(organizationName, slug);

        return new OrganizationResult(uuid, organizationName);
    }

    public record OrganizationResult(
            UUID organizationId,
            String organizationName
    ) {
    }
}
