package com.pl.hragency.testsupport;
import com.pl.hragency.sales.application.command.CreateSalesOpportunityActivityCommand;
import com.pl.hragency.sales.application.handler.CreateSalesOpportunityActivityHandler;
import com.pl.hragency.sales.domain.model.SalesActivityType;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;
import com.pl.hragency.shared.rest.ExecutionContext;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;


@Component
public class TestSalesOpportunityActivityFactory {
    private final CreateSalesOpportunityActivityHandler handler;
    private final Faker faker;

    public TestSalesOpportunityActivityFactory(CreateSalesOpportunityActivityHandler handler) {
        this.handler = handler;
        this.faker = new Faker(Locale.forLanguageTag("pl"));
    }

    public UUID create(
            UUID organizationId,
            UUID opportunityId,
            UUID userId)
    {
        return create(organizationId, opportunityId, userId,
                faker.bigBangTheory().quote(),
                SalesActivityType.CALL);
    }

    public UUID create(
            UUID organizationId,
            UUID opportunityId,
            UUID userId,
            String note,
            SalesActivityType type
            ) {

        var command = new CreateSalesOpportunityActivityCommand(note, type);

        return handler.handle(
                new ExecutionContext(organizationId, userId, "Test user"),
                new SalesOpportunityId(opportunityId),
                command
        ).value();
    }
}
