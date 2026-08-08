package com.pl.hragency.development.scenario.sales;
import com.pl.hragency.company.api.CompanyApi;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.sales.api.ChangeSalesOpportunityStageInput;
import com.pl.hragency.sales.api.CreateSalesOpportunityInput;
import com.pl.hragency.sales.api.SalesApi;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Component
public class SalesOpportunityScenario {
    private final SalesApi  salesApi;
    private final IdentityApi identityApi;
    private final CompanyApi companyApi;

    public SalesOpportunityScenario(SalesApi salesApi, IdentityApi identityApi, CompanyApi companyApi) {
        this.salesApi = salesApi;
        this.identityApi = identityApi;
        this.companyApi = companyApi;
    }

    public void createOpportunities(
            UUID organizationId)
    {
        var users = identityApi.findUserSuggestions(organizationId, "", Set.of("SALES"));
        var companies = companyApi.findCompanySuggestions(organizationId, "", null);

        int index = 0;

        UUID userId = users.get(++index % users.size()).id();
        UUID companyId = companies.get(++index % companies.size()).id();

        var qualified  = salesApi.createOpportunity(organizationId,
                userId,
                new CreateSalesOpportunityInput(companyId,
                        "Recruitment services for production workers",
                        "Recruitment of welders and production workers.",
                        new BigDecimal("45000.00"),
                        "EUR",
                        LocalDate.now().plusDays(30),
                        userId)
                );

        salesApi.changeOpportunityStage(
                organizationId,
                userId,
                new ChangeSalesOpportunityStageInput(qualified,
                        "QUALIFIED", null)
        );

        userId = users.get(++index % users.size()).id();
        companyId = companies.get(++index % companies.size()).id();

        var proposal   = salesApi.createOpportunity(organizationId,
                userId,
                new CreateSalesOpportunityInput(companyId,
                        "Temporary workers recruitment",
                        "Recruitment of temporary production workers.",
                        new BigDecimal("28000.00"),
                        "EUR",
                        LocalDate.now().plusDays(30),
                        userId)
        );

        salesApi.changeOpportunityStage(
                organizationId,
                userId,
                new ChangeSalesOpportunityStageInput(proposal,
                        "QUALIFIED", null)
        );

        salesApi.changeOpportunityStage(
                organizationId,
                userId,
                new ChangeSalesOpportunityStageInput(proposal,
                        "PROPOSAL", null)
        );

        salesApi.createOpportunity(organizationId,
                userId,
                new CreateSalesOpportunityInput(companyId,
                        "Warehouse workers recruitment",
                        "Recruitment of warehouse and logistics workers.",
                        new BigDecimal("32000.00"),
                        "EUR",
                        LocalDate.now().plusDays(60),
                        userId)
        );

        userId = users.get(++index % users.size()).id();
        companyId = companies.get(++index % companies.size()).id();

        var won   = salesApi.createOpportunity(organizationId,
                userId,
                new CreateSalesOpportunityInput(companyId,
                        "Welding specialists recruitment",
                        "Long-term recruitment project for qualified MAG welders.",
                        new BigDecimal("65000.00"),
                        "EUR",
                        LocalDate.now().plusDays(90),
                        userId)
        );

        salesApi.changeOpportunityStage(
                organizationId,
                userId,
                new ChangeSalesOpportunityStageInput(won,
                        "QUALIFIED", null)
        );

        salesApi.changeOpportunityStage(
                organizationId,
                userId,
                new ChangeSalesOpportunityStageInput(won,
                        "PROPOSAL", null)
        );

        salesApi.changeOpportunityStage(
                organizationId,
                userId,
                new ChangeSalesOpportunityStageInput(won,
                        "WON", null)
        );

        var lost   = salesApi.createOpportunity(organizationId,
                userId,
                new CreateSalesOpportunityInput(companyId,
                        "Production line staffing",
                        "Complete staffing solution for a new production line.",
                        new BigDecimal("85000.00"),
                        "EUR",
                        LocalDate.now().plusDays(75),
                        userId)
        );

        salesApi.changeOpportunityStage(
                organizationId,
                userId,
                new ChangeSalesOpportunityStageInput(lost,
                        "LOST",   "Client selected another recruitment agency.")
        );

    }
}
