package com.pl.hragency.testsupport;
import com.pl.hragency.sales.application.command.CreateSalesOpportunityCommand;
import com.pl.hragency.sales.application.handler.CreateSalesOpportunityHandler;
import com.pl.hragency.shared.rest.ExecutionContext;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;


@Component
public class TestSalesOpportunityFactory {
    private final CreateSalesOpportunityHandler handler;
    private final Faker faker;

    public TestSalesOpportunityFactory(CreateSalesOpportunityHandler handler) {
        this.handler = handler;
        this.faker = new Faker(Locale.forLanguageTag("pl"));
    }

    public UUID create(
            UUID organizationId,
            UUID companyId,
            UUID userId
    ) {
        return create(organizationId,
                companyId,
                faker.southPark().quotes(),
                faker.text().text(200),
                BigDecimal.valueOf(20_000),
                userId);
    }
    public UUID create(
            UUID organizationId,
            UUID companyId,
            String title,
            String description,
            BigDecimal exceptedValue,
            UUID userId) {

        var command = new CreateSalesOpportunityCommand(companyId, title, description, exceptedValue,
                "PLN", LocalDate.now().plusDays(60),
                userId);

        return handler.handle(
                new ExecutionContext(organizationId, userId, "Test user"),
                command
        ).value();
    }

}
