package com.pl.hragency.recruitment.timeline;

import com.pl.hragency.BaseIntegrationTest;
import com.pl.hragency.recruitment.domain.event.*;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationStatus;
import com.pl.hragency.recruitment.domain.model.candidate.Candidate;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import com.pl.hragency.recruitment.domain.model.interview.InterviewStatus;
import com.pl.hragency.recruitment.timeline.application.port.CandidateTimelineRepository;
import com.pl.hragency.recruitment.timeline.model.CandidateTimelineEntry;
import com.pl.hragency.recruitment.timeline.model.CandidateTimelineType;
import com.pl.hragency.shared.event.EventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.awaitility.Awaitility.await;

class CandidateTimelineProjectionTest extends BaseIntegrationTest {

    private static final String CANDIDATE_EMAIL = "j.smith@fake.io";
    private static final String JOB_TITLE = "Java Developer";

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private CandidateTimelineRepository timelineRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    void shouldBuildCandidateTimelineFromPublishedEvents() {
        var candidateId = UUID.randomUUID();
        var organizationId = UUID.randomUUID();
        var userId = UUID.randomUUID();
        var applicationId = UUID.randomUUID();
        var occurredAt = Instant.now();
        var interViewId = UUID.randomUUID();

        publishCandidateEvents(
                candidateId,
                organizationId,
                userId,
                applicationId,
                interViewId,
                occurredAt
        );

        await()
                .atMost(Duration.ofSeconds(5))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    var timeline = timelineRepository.findByCandidate(
                            organizationId,
                            candidateId,
                            PageRequest.of(0, 100)
                    );

                    assertThat(timeline)
                            .hasSize(8);
                    assertThat(timeline)
                            .extracting(CandidateTimelineEntry::type)
                            .containsExactlyInAnyOrder(
                                    CandidateTimelineType.CANDIDATE_CREATED,
                                    CandidateTimelineType.APPLICATION_CREATED,
                                    CandidateTimelineType.APPLICATION_STATUS_CHANGED,
                                    CandidateTimelineType.INTERVIEW_SCHEDULED,
                                    CandidateTimelineType.INTERVIEW_COMPLETED,
                                    CandidateTimelineType.APPLICATION_STATUS_CHANGED,
                                    CandidateTimelineType.APPLICATION_STATUS_CHANGED,
                                    CandidateTimelineType.CANDIDATE_HIRED
                            );
                });
    }

    private void publishCandidateEvents(
            UUID candidateId,
            UUID organizationId,
            UUID userId,
            UUID applicationId,
            UUID interViewId,
            Instant occurredAt
    ) {
        transactionTemplate.executeWithoutResult(status -> {
            eventPublisher.publish(
                    new CandidateCreatedEvent(
                            candidateId,
                            organizationId,
                            "John",
                            "Smith",
                            CANDIDATE_EMAIL,
                            CandidateSource.DIRECT_APPLICATION,
                            userId,
                            "",
                            occurredAt
                    )
            );

            eventPublisher.publish(
                    new JobApplicationCreatedEvent(
                            applicationId,
                            UUID.randomUUID(),
                            JOB_TITLE,
                            organizationId,
                            candidateId,
                            CANDIDATE_EMAIL,
                            CandidateSource.DIRECT_APPLICATION,
                            userId,
                            "",
                            occurredAt
                    )
            );

            eventPublisher.publish(
                    new JobApplicationStatusChangedEvent(
                            applicationId,
                            candidateId,
                            organizationId,
                            JobApplicationStatus.APPLIED,
                            JobApplicationStatus.ASSESSMENT,
                            userId,
                            "",
                            occurredAt
                    )
            );

            eventPublisher.publish(
                    new InterviewScheduledEvent(interViewId,
                            organizationId,
                            candidateId,
                            applicationId,
                            userId,
                            LocalDateTime.now(),
                            "",
                            occurredAt
                    )
            );

            eventPublisher.publish(
                    new InterviewStatusChangedEvent(interViewId,
                            organizationId,
                            candidateId,
                            applicationId,
                            InterviewStatus.PLANNED,
                            InterviewStatus.COMPLETED,
                            userId,
                            "",
                            occurredAt
                    )
            );

            eventPublisher.publish(
                    new JobApplicationStatusChangedEvent(
                            applicationId,
                            candidateId,
                            organizationId,
                            JobApplicationStatus.ASSESSMENT,
                            JobApplicationStatus.OFFER,
                            userId,
                            "",
                            occurredAt
                    )
            );

            eventPublisher.publish(
                    new JobApplicationStatusChangedEvent(
                            applicationId,
                            candidateId,
                            organizationId,
                            JobApplicationStatus.OFFER,
                            JobApplicationStatus.HIRED,
                            userId,
                            "",
                            occurredAt
                    )
            );

            eventPublisher.publish(
                    new CandidateHiredEvent(
                            candidateId,
                            applicationId,
                            JOB_TITLE,
                            UUID.randomUUID(),
                            organizationId,
                            userId,
                            "",
                            occurredAt
                    )
            );

        });
    }
}