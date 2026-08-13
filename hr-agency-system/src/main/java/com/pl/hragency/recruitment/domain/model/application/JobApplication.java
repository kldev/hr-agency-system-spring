package com.pl.hragency.recruitment.domain.model.application;

import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;

import java.time.Instant;
import java.util.UUID;

public final class JobApplication {

    private final JobApplicationId id;
    private final UUID organizationId;
    private final JobPostingId jobPostingId;
    private final CandidateId candidateId;

    private JobApplicationStatus status;
    private CandidateSource source;

    private final Instant createdAt;
    private Instant updatedAt;

    private JobApplication(
            JobApplicationId id,
            UUID organizationId,
            JobPostingId jobPostingId,
            CandidateId candidateId,
            JobApplicationStatus status,
            CandidateSource source,
            Instant createdAt,
            Instant updatedAt) {

        this.id = id;
        this.organizationId = organizationId;
        this.jobPostingId = jobPostingId;
        this.candidateId = candidateId;
        this.status = status;
        this.source = source;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static JobApplication create(
            UUID organizationId,
            JobPostingId jobPostingId,
            CandidateId candidateId, CandidateSource source) {

        var now = Instant.now();

        return new JobApplication(
                JobApplicationId.newId(),
                organizationId,
                jobPostingId,
                candidateId,
                JobApplicationStatus.APPLIED,
                source,
                now,
                now
        );
    }

    public static JobApplication rehydrate(
            JobApplicationId id,
            UUID organizationId,
            JobPostingId jobPostingId,
            CandidateId candidateId,
            JobApplicationStatus status,
            CandidateSource source,
            Instant createdAt,
            Instant updatedAt) {

        return new JobApplication(
                id,
                organizationId,
                jobPostingId,
                candidateId,
                status,
                source,
                createdAt,
                updatedAt
        );
    }

    public void startScreening() {
        requireStatus(JobApplicationStatus.APPLIED);

        status = JobApplicationStatus.SCREENING;
        touch();
    }

    public void scheduleInterview() {
        requireStatus(
                JobApplicationStatus.SCREENING,
                JobApplicationStatus.ASSESSMENT
        );

        status = JobApplicationStatus.INTERVIEW;
        touch();
    }

    public void startAssessment() {
        requireStatus(
                JobApplicationStatus.SCREENING,
                JobApplicationStatus.INTERVIEW
        );

        status = JobApplicationStatus.ASSESSMENT;
        touch();
    }

    public void makeOffer() {
        requireStatus(
                JobApplicationStatus.INTERVIEW,
                JobApplicationStatus.ASSESSMENT
        );

        status = JobApplicationStatus.OFFER;
        touch();
    }

    public void hire() {
        requireStatus(JobApplicationStatus.OFFER);

        status = JobApplicationStatus.HIRED;
        touch();
    }

    public void reject() {
        requireNotFinal();

        status = JobApplicationStatus.REJECTED;
        touch();
    }

    public void withdraw() {
        requireNotFinal();

        status = JobApplicationStatus.WITHDRAWN;
        touch();
    }

    private void requireStatus(JobApplicationStatus... allowedStatuses) {
        for (JobApplicationStatus allowedStatus : allowedStatuses) {
            if (status == allowedStatus) {
                return;
            }
        }

        throw new IllegalStateException(
                "Cannot change application status from "
                        + status
        );
    }

    private void requireNotFinal() {
        if (status == JobApplicationStatus.HIRED
                || status == JobApplicationStatus.REJECTED
                || status == JobApplicationStatus.WITHDRAWN) {

            throw new IllegalStateException(
                    "Application is already in final status: " + status
            );
        }
    }

    private void touch() {
        updatedAt = Instant.now();
    }

    public JobApplicationId id() {
        return id;
    }

    public UUID organizationId() {
        return organizationId;
    }

    public JobPostingId jobPostingId() {
        return jobPostingId;
    }

    public CandidateId candidateId() {
        return candidateId;
    }

    public JobApplicationStatus status() {
        return status;
    }

    public  CandidateSource source() {return source;}

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}