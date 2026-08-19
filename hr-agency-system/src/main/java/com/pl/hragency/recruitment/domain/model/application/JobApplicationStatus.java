package com.pl.hragency.recruitment.domain.model.application;

public enum JobApplicationStatus {

    /**
     * Candidate has submitted an application for the job posting.
     */
    APPLIED,

    /**
     * Application is being reviewed by a recruiter.
     */
    SCREENING,

    /**
     * Candidate is participating in an interview process.
     */
    INTERVIEW,

    /**
     * Candidate is completing an assessment, test or other evaluation.
     */
    ASSESSMENT,

    /**
     * An employment offer has been made to the candidate.
     */
    OFFER,

    /**
     * Candidate has accepted the offer and has been hired.
     */
    HIRED,

    /**
     * Application has been rejected by the recruitment team.
     */
    REJECTED,

    /**
     * Candidate has withdrawn their application.
     */
    WITHDRAWN,

    IN_REVIEW
}
