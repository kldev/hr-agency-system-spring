package com.pl.hragency.development;

import com.pl.hragency.development.scenario.CompanyScenario;
import com.pl.hragency.development.scenario.JobDescriptionScenario;
import com.pl.hragency.development.scenario.OrganizationScenario;
import com.pl.hragency.development.scenario.UserScenario;
import com.pl.hragency.development.scenario.sales.SalesOpportunityScenario;
import org.springframework.stereotype.Component;

@Component
public class DevelopmentScenario {
    private final OrganizationScenario  organizationScenario;
    private final UserScenario userScenario;
    private final CompanyScenario companyScenario;
    private final JobDescriptionScenario jobDescriptionScenario;
    private final SalesOpportunityScenario salesOpportunityScenario;

    public DevelopmentScenario(OrganizationScenario organizationScenario,
                               UserScenario userScenario,
                               CompanyScenario companyScenario,
                               JobDescriptionScenario jobDescriptionScenario,
                               SalesOpportunityScenario salesOpportunityScenario) {
        this.organizationScenario = organizationScenario;
        this.userScenario = userScenario;
        this.companyScenario = companyScenario;
        this.jobDescriptionScenario = jobDescriptionScenario;
        this.salesOpportunityScenario = salesOpportunityScenario;
    }

    public void create() {
        var result = organizationScenario.create();
        var usersIds = userScenario.create(result.organizationId());
        companyScenario.createCompanies(result.organizationId());
        jobDescriptionScenario.create(result.organizationId(), usersIds);
        salesOpportunityScenario.createOpportunities(result.organizationId());
    }
}
