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

    public OrganizationResult create() {
       UUID uuid = api.create("HR Agency z.o.o", "hr-agency");

       return new OrganizationResult(uuid, "HR Agency z.o.o");
    }

    public record OrganizationResult(
            UUID organizationId,
            String organizationName
    ) {
    }
}
