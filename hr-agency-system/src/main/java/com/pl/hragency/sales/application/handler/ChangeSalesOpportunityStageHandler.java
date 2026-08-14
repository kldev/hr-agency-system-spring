package com.pl.hragency.sales.application.handler;

import com.pl.hragency.sales.application.command.ChangeSalesOpportunityStageCommand;
import com.pl.hragency.sales.application.port.SalesOpportunityRepository;
import com.pl.hragency.sales.domain.event.SalesOpportunityLostEvent;
import com.pl.hragency.sales.domain.event.SalesOpportunityStageChangedEvent;
import com.pl.hragency.sales.domain.event.SalesOpportunityWonEvent;
import com.pl.hragency.sales.domain.model.SalesOpportunity;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;
import com.pl.hragency.sales.domain.model.SalesOpportunityStage;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class ChangeSalesOpportunityStageHandler {

    private final SalesOpportunityRepository repository;
    private final EventPublisher eventPublisher;

    public ChangeSalesOpportunityStageHandler(
            SalesOpportunityRepository repository,
            EventPublisher eventPublisher
    ) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public void handle(
            ExecutionContext context,
            SalesOpportunityId  salesOpportunityId,
            ChangeSalesOpportunityStageCommand command
    ) {
        var opportunity = repository
                .findById(
                        context.organizationId(),
                        salesOpportunityId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Sales opportunity not found"
                        )
                );

        SalesOpportunityStage previousStage = opportunity.stage();

        opportunity.changeStage(
                command.stage(),
                command.lostReason()
        );

        var updated = repository.updateStage(
                context.organizationId(),
                opportunity.id(),
                opportunity.stage(),
                opportunity.lostReason()
        );

        if (updated != 1) {
            throw new IllegalStateException(
                    "Sales opportunity was not updated"
            );
        }

        var event   = new SalesOpportunityStageChangedEvent(
                context.organizationId(),
                opportunity.id().value(),
                opportunity.companyId(),
                previousStage,
                opportunity.stage(),
                opportunity.salesOwnerId(),
                context.userId(),
                context.fullName(),
                Instant.now()
        );

        eventPublisher.publish(event);

        publishSpecificEvent(
                opportunity,
                command.lostReason(),
                context

        );
    }

    private void publishSpecificEvent(
            SalesOpportunity opportunity,
            String lostReason,
            ExecutionContext context
    ) {
        switch (opportunity.stage()) {
            case WON -> eventPublisher.publish(
                    new SalesOpportunityWonEvent(
                            opportunity.organizationId(),
                            opportunity.id().value(),
                            opportunity.companyId(),
                            opportunity.salesOwnerId(),
                            context.userId(),
                            context.fullName(),
                            Instant.now()
                            )
            );

            case LOST -> eventPublisher.publish(
                    new SalesOpportunityLostEvent(
                            opportunity.organizationId(),
                            opportunity.id().value(),
                            opportunity.companyId(),
                            opportunity.salesOwnerId(),
                            lostReason,
                            context.userId(),
                            context.fullName(),
                            Instant.now()
                    )
            );

            default -> {
            }
        }
    }
}