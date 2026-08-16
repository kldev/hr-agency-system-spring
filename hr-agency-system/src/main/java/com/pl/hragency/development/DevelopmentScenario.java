package com.pl.hragency.development;

import com.pl.hragency.development.scenario.CompanyScenario;
import com.pl.hragency.development.scenario.JobDescriptionScenario;
import com.pl.hragency.development.scenario.OrganizationScenario;
import com.pl.hragency.development.scenario.UserScenario;
import com.pl.hragency.development.scenario.jobposting.JobPostingScenario;
import com.pl.hragency.development.scenario.sales.SalesOpportunityScenario;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class DevelopmentScenario {
    private final Logger logger = LoggerFactory.getLogger(DevelopmentScenario.class);
    private final OrganizationScenario  organizationScenario;
    private final UserScenario userScenario;
    private final CompanyScenario companyScenario;
    private final JobDescriptionScenario jobDescriptionScenario;
    private final SalesOpportunityScenario salesOpportunityScenario;
    private final JobPostingScenario jobPostingScenario;
    private final Faker faker;

    public DevelopmentScenario(OrganizationScenario organizationScenario,
                               UserScenario userScenario,
                               CompanyScenario companyScenario,
                               JobDescriptionScenario jobDescriptionScenario,
                               SalesOpportunityScenario salesOpportunityScenario,
                               JobPostingScenario jobPostingScenario) {
        this.organizationScenario = organizationScenario;
        this.userScenario = userScenario;
        this.companyScenario = companyScenario;
        this.jobDescriptionScenario = jobDescriptionScenario;
        this.salesOpportunityScenario = salesOpportunityScenario;
        this.jobPostingScenario = jobPostingScenario;
        this.faker = new Faker(Locale.forLanguageTag("pl"));
    }

    public void create() {

        userScenario.createPlatformOwner();

        create("HR Agency z.o.o", "hr-agency");
        create("Flex Jobs z.o.o", "flex-jobs");
        create("Demo Jobs z.o.o", "demo");
        create("We Help .inc", "we-help");

        // seedExtra();

    }
    private void seedExtra() {
        try (var executor = Executors.newFixedThreadPool(32)) {
            ;

            try {
                List<Future<?>> futures = new ArrayList<>();

                for (int i = 1; i <= 500; i++) {
                    int index = i;

                    futures.add(executor.submit(() -> {
                        try {
                            String company = faker.company().name();
                            String slug = createSlug(company, index);

                            create(company, slug);

                            logger.info(
                                    "Created organization {} ({})",
                                    index,
                                    company
                            );
                        } catch (Exception e) {
                            logger.error(
                                    "Failed to create organization {}",
                                    index,
                                    e
                            );
                        }
                    }));
                }

                for (Future<?> future : futures) {
                    try {
                        future.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException(
                                "Development data generation interrupted",
                                e
                        );
                    } catch (ExecutionException e) {
                        logger.error(
                                "Development data generation task failed",
                                e.getCause()
                        );
                    }
                }

            } finally {
                executor.shutdown();
            }
        }
        catch (Exception e) {
            logger.error(
                    "Development data generation task failed",
                    e.getCause()
            );
        }
    }

    private String createSlug(String company, int index) {
        return company
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "")
                + "-" + index;
    }

    public void create(String organizationName, String slug) {
        var result = organizationScenario.create(organizationName, slug);
        var usersIds = userScenario.create(result.organizationId(), slug);
        companyScenario.createCompanies(result.organizationId());
        jobDescriptionScenario.create(result.organizationId(), usersIds);
        salesOpportunityScenario.createOpportunities(result.organizationId());
        jobPostingScenario.create(result.organizationId());
    }
}
