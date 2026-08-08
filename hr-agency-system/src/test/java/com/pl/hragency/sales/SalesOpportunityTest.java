package com.pl.hragency.sales;

import com.pl.hragency.sales.domain.event.SalesOpportunityStageChanged;
import com.pl.hragency.sales.domain.model.SalesOpportunity;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;
import com.pl.hragency.sales.domain.model.SalesOpportunityStage;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SalesOpportunityTest {

    private static final UUID ORGANIZATION_ID =
            UUID.randomUUID();

    private static final UUID COMPANY_ID =
            UUID.randomUUID();

    private static final UUID SALES_OWNER_ID =
            UUID.randomUUID();

    @Test
    void shouldCreateSalesOpportunityInNewStage() {

        // when
        var opportunity = createOpportunity();

        // then
        assertThat(opportunity.id())
                .isNotNull();

        assertThat(opportunity.organizationId())
                .isEqualTo(ORGANIZATION_ID);

        assertThat(opportunity.companyId())
                .isEqualTo(COMPANY_ID);

        assertThat(opportunity.title())
                .isEqualTo("Java Recruitment");

        assertThat(opportunity.stage())
                .isEqualTo(SalesOpportunityStage.NEW);

        assertThat(opportunity.lostReason())
                .isNull();

        assertThat(opportunity.salesOwnerId())
                .isEqualTo(SALES_OWNER_ID);

        assertThat(opportunity.createdAt())
                .isNotNull();
    }

    @Test
    void shouldChangeNewToContacted() {

        // given
        var opportunity = createOpportunity();

        // when
        var event = opportunity.changeStage(
                SalesOpportunityStage.CONTACTED,
                null
        );

        // then
        assertStageChanged(
                opportunity,
                event,
                SalesOpportunityStage.NEW,
                SalesOpportunityStage.CONTACTED
        );
    }

    @Test
    void shouldChangeContactedToQualified() {

        // given
        var opportunity = createOpportunity();

        opportunity.changeStage(
                SalesOpportunityStage.CONTACTED,
                null
        );

        // when
        var event = opportunity.changeStage(
                SalesOpportunityStage.QUALIFIED,
                null
        );

        // then
        assertStageChanged(
                opportunity,
                event,
                SalesOpportunityStage.CONTACTED,
                SalesOpportunityStage.QUALIFIED
        );
    }

    @Test
    void shouldChangeQualifiedToProposal() {

        // given
        var opportunity = createOpportunity();

        moveTo(
                opportunity,
                SalesOpportunityStage.CONTACTED
        );

        moveTo(
                opportunity,
                SalesOpportunityStage.QUALIFIED
        );

        // when
        var event = opportunity.changeStage(
                SalesOpportunityStage.PROPOSAL,
                null
        );

        // then
        assertStageChanged(
                opportunity,
                event,
                SalesOpportunityStage.QUALIFIED,
                SalesOpportunityStage.PROPOSAL
        );
    }

    @Test
    void shouldChangeProposalToWon() {

        // given
        var opportunity = createOpportunity();

        moveToProposal(opportunity);

        // when
        var event = opportunity.changeStage(
                SalesOpportunityStage.WON,
                null
        );

        // then
        assertStageChanged(
                opportunity,
                event,
                SalesOpportunityStage.PROPOSAL,
                SalesOpportunityStage.WON
        );

        assertThat(opportunity.lostReason())
                .isNull();
    }

    @Test
    void shouldChangeProposalToLostWithReason() {

        // given
        var opportunity = createOpportunity();

        moveToProposal(opportunity);

        var lostReason =
                "Customer selected another recruitment agency";

        // when
        var event = opportunity.changeStage(
                SalesOpportunityStage.LOST,
                lostReason
        );

        // then
        assertStageChanged(
                opportunity,
                event,
                SalesOpportunityStage.PROPOSAL,
                SalesOpportunityStage.LOST
        );

        assertThat(opportunity.lostReason())
                .isEqualTo(lostReason);
    }

    @Test
    void shouldRejectNullStage() {

        // given
        var opportunity = createOpportunity();

        // when / then
        assertThatThrownBy(() ->
                opportunity.changeStage(null, null)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Stage cannot be null");

        assertThat(opportunity.stage())
                .isEqualTo(SalesOpportunityStage.NEW);
    }

    @Test
    void shouldRejectChangingToSameStage() {

        // given
        var opportunity = createOpportunity();

        // when / then
        assertThatThrownBy(() ->
                opportunity.changeStage(
                        SalesOpportunityStage.NEW,
                        null
                )
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Sales opportunity is already in stage NEW"
                );

        assertThat(opportunity.stage())
                .isEqualTo(SalesOpportunityStage.NEW);
    }

    @Test
    void shouldRejectNewToQualified() {

        // given
        var opportunity = createOpportunity();

        // when / then
        assertInvalidTransition(
                opportunity,
                SalesOpportunityStage.QUALIFIED
        );
    }

    @Test
    void shouldRejectNewToProposal() {

        // given
        var opportunity = createOpportunity();

        // when / then
        assertInvalidTransition(
                opportunity,
                SalesOpportunityStage.PROPOSAL
        );
    }

    @Test
    void shouldRejectNewToWon() {

        // given
        var opportunity = createOpportunity();

        // when / then
        assertInvalidTransition(
                opportunity,
                SalesOpportunityStage.WON
        );
    }

    @Test
    void shouldRejectNewToLost() {

        // given
        var opportunity = createOpportunity();

        // when / then
        assertInvalidTransition(
                opportunity,
                SalesOpportunityStage.LOST
        );
    }

    @Test
    void shouldRejectContactedToProposal() {

        // given
        var opportunity = createOpportunity();

        moveTo(
                opportunity,
                SalesOpportunityStage.CONTACTED
        );

        // when / then
        assertInvalidTransition(
                opportunity,
                SalesOpportunityStage.PROPOSAL
        );
    }

    @Test
    void shouldRejectContactedToWon() {

        // given
        var opportunity = createOpportunity();

        moveTo(
                opportunity,
                SalesOpportunityStage.CONTACTED
        );

        // when / then
        assertInvalidTransition(
                opportunity,
                SalesOpportunityStage.WON
        );
    }

    @Test
    void shouldRejectContactedToLost() {

        // given
        var opportunity = createOpportunity();

        moveTo(
                opportunity,
                SalesOpportunityStage.CONTACTED
        );

        // when / then
        assertInvalidTransition(
                opportunity,
                SalesOpportunityStage.LOST
        );
    }

    @Test
    void shouldRejectQualifiedToWon() {

        // given
        var opportunity = createOpportunity();

        opportunity.changeStage(
                SalesOpportunityStage.CONTACTED,
                null
        );

        opportunity.changeStage(
                SalesOpportunityStage.QUALIFIED,
                null
        );

        // when / then
        assertInvalidTransition(
                opportunity,
                SalesOpportunityStage.WON
        );
    }

    @Test
    void shouldRejectQualifiedToLost() {

        // given
        var opportunity = createOpportunity();

        opportunity.changeStage(
                SalesOpportunityStage.CONTACTED,
                null
        );

        opportunity.changeStage(
                SalesOpportunityStage.QUALIFIED,
                null
        );

        // when / then
        assertInvalidTransition(
                opportunity,
                SalesOpportunityStage.LOST
        );
    }

    @Test
    void shouldRejectProposalToContacted() {

        // given
        var opportunity = createOpportunity();

        moveToProposal(opportunity);

        // when / then
        assertInvalidTransition(
                opportunity,
                SalesOpportunityStage.CONTACTED
        );
    }

    @Test
    void shouldRejectProposalToQualified() {

        // given
        var opportunity = createOpportunity();

        moveToProposal(opportunity);

        // when / then
        assertInvalidTransition(
                opportunity,
                SalesOpportunityStage.QUALIFIED
        );
    }

    @Test
    void shouldRejectChangingStageAfterWon() {

        // given
        var opportunity = createOpportunity();

        moveToProposal(opportunity);

        opportunity.changeStage(
                SalesOpportunityStage.WON,
                null
        );

        // when / then
        assertInvalidTransition(
                opportunity,
                SalesOpportunityStage.PROPOSAL
        );

        assertThat(opportunity.stage())
                .isEqualTo(SalesOpportunityStage.WON);
    }

    @Test
    void shouldRejectChangingStageAfterLost() {

        // given
        var opportunity = createOpportunity();

        moveToProposal(opportunity);

        opportunity.changeStage(
                SalesOpportunityStage.LOST,
                "Customer selected another agency"
        );

        // when / then
        assertInvalidTransition(
                opportunity,
                SalesOpportunityStage.PROPOSAL
        );

        assertThat(opportunity.stage())
                .isEqualTo(SalesOpportunityStage.LOST);

        assertThat(opportunity.lostReason())
                .isEqualTo(
                        "Customer selected another agency"
                );
    }

    @Test
    void shouldRequireLostReasonWhenChangingToLost() {

        // given
        var opportunity = createOpportunity();

        moveToProposal(opportunity);

        // when / then
        assertThatThrownBy(() ->
                opportunity.changeStage(
                        SalesOpportunityStage.LOST,
                        null
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Lost reason is required when opportunity is lost"
                );

        assertThat(opportunity.stage())
                .isEqualTo(SalesOpportunityStage.PROPOSAL);

        assertThat(opportunity.lostReason())
                .isNull();
    }

    @Test
    void shouldRejectBlankLostReason() {

        // given
        var opportunity = createOpportunity();

        moveToProposal(opportunity);

        // when / then
        assertThatThrownBy(() ->
                opportunity.changeStage(
                        SalesOpportunityStage.LOST,
                        "   "
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Lost reason is required when opportunity is lost"
                );

        assertThat(opportunity.stage())
                .isEqualTo(SalesOpportunityStage.PROPOSAL);

        assertThat(opportunity.lostReason())
                .isNull();
    }

    @Test
    void shouldKeepLostReasonWhenOpportunityIsLost() {

        // given
        var opportunity = createOpportunity();

        moveToProposal(opportunity);

        var lostReason = "Budget was cancelled";

        // when
        opportunity.changeStage(
                SalesOpportunityStage.LOST,
                lostReason
        );

        // then
        assertThat(opportunity.stage())
                .isEqualTo(SalesOpportunityStage.LOST);

        assertThat(opportunity.lostReason())
                .isEqualTo(lostReason);
    }

    @Test
    void shouldClearLostReasonWhenChangingToNonLostStage() {

        // given
        var opportunity = SalesOpportunity.rehydrate(
                SalesOpportunityId.newId(),
                ORGANIZATION_ID,
                COMPANY_ID,
                "Java Recruitment",
                "Recruitment project",
                SalesOpportunityStage.QUALIFIED,
                new BigDecimal("25000"),
                "EUR",
                LocalDate.of(2026, 12, 31),
                "Previous lost reason",
                SALES_OWNER_ID,
                Instant.now()
        );

        // when
        opportunity.changeStage(
                SalesOpportunityStage.PROPOSAL,
                null
        );

        // then
        assertThat(opportunity.stage())
                .isEqualTo(SalesOpportunityStage.PROPOSAL);

        assertThat(opportunity.lostReason())
                .isNull();
    }

    private SalesOpportunity createOpportunity() {
        return SalesOpportunity.create(
                ORGANIZATION_ID,
                COMPANY_ID,
                "Java Recruitment",
                "Recruitment project",
                new BigDecimal("25000"),
                "EUR",
                LocalDate.of(2026, 12, 31),
                SALES_OWNER_ID
        );
    }

    private void moveToProposal(
            SalesOpportunity opportunity
    ) {
        opportunity.changeStage(
                SalesOpportunityStage.CONTACTED,
                null
        );

        opportunity.changeStage(
                SalesOpportunityStage.QUALIFIED,
                null
        );

        opportunity.changeStage(
                SalesOpportunityStage.PROPOSAL,
                null
        );
    }

    private void moveTo(
            SalesOpportunity opportunity,
            SalesOpportunityStage stage
    ) {
        opportunity.changeStage(
                stage,
                null
        );
    }

    private void assertInvalidTransition(
            SalesOpportunity opportunity,
            SalesOpportunityStage newStage
    ) {
        var currentStage = opportunity.stage();

        assertThatThrownBy(() ->
                opportunity.changeStage(
                        newStage,
                        null
                )
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Cannot change opportunity stage from "
                                + currentStage
                                + " to "
                                + newStage
                );

        assertThat(opportunity.stage())
                .isEqualTo(currentStage);
    }

    private void assertStageChanged(
            SalesOpportunity opportunity,
            SalesOpportunityStageChanged event,
            SalesOpportunityStage previousStage,
            SalesOpportunityStage newStage
    ) {
        assertThat(opportunity.stage())
                .isEqualTo(newStage);

        assertThat(event)
                .isNotNull();

        assertThat(event.organizationId())
                .isEqualTo(ORGANIZATION_ID);

        assertThat(event.salesOpportunityId())
                .isEqualTo(opportunity.id().value());

        assertThat(event.companyId())
                .isEqualTo(COMPANY_ID);

        assertThat(event.previousStage())
                .isEqualTo(previousStage);

        assertThat(event.newStage())
                .isEqualTo(newStage);

        assertThat(event.salesOwnerId())
                .isEqualTo(SALES_OWNER_ID);

        assertThat(event.occurredAt())
                .isNotNull();
    }
}