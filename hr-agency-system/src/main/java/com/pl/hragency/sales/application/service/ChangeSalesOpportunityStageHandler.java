package com.pl.hragency.sales.application.service;

import com.pl.hragency.sales.application.command.ChangeSalesOpportunityStageCommand;
import com.pl.hragency.sales.application.port.SalesOpportunityRepository;
import com.pl.hragency.sales.domain.event.SalesOpportunityLost;
import com.pl.hragency.sales.domain.event.SalesOpportunityStageChanged;
import com.pl.hragency.sales.domain.event.SalesOpportunityWon;
import com.pl.hragency.sales.domain.model.SalesOpportunity;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;
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

        var event = opportunity.changeStage(
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

        eventPublisher.publish(event);

        publishSpecificEvent(
                opportunity,
                command.lostReason()
        );
    }

    private void publishSpecificEvent(
            SalesOpportunity opportunity,
            String lostReason
    ) {
        switch (opportunity.stage()) {
            case WON -> eventPublisher.publish(
                    new SalesOpportunityWon(
                            opportunity.organizationId(),
                            opportunity.id().value(),
                            opportunity.companyId(),
                            opportunity.salesOwnerId(), Instant.now()
                            )
            );

            case LOST -> eventPublisher.publish(
                    new SalesOpportunityLost(
                            opportunity.organizationId(),
                            opportunity.id().value(),
                            opportunity.companyId(),
                            opportunity.salesOwnerId(),
                            lostReason,
                            Instant.now()
                    )
            );

            default -> {
            }
        }
    }
}