package com.pl.hragency.recruitment.domain.model.candidate;

public enum CandidateSource {

    /**
     * Candidate applied through company's career page.
     */
    CAREER_PAGE,

    /**
     * Candidate came from Pracuj.pl.
     */
    PRACUJ_PL,

    /**
     * Candidate came from OLX Praca.
     */
    OLX,

    /**
     * Candidate came from Praca.pl.
     */
    PRACA_PL,

    /**
     * Candidate came from RocketJobs.pl.
     */
    ROCKETJOBS,

    /**
     * Candidate came from Just Join IT.
     */
    JUST_JOIN_IT,

    /**
     * Candidate came from No Fluff Jobs.
     */
    NO_FLUFF_JOBS,

    /**
     * Candidate came from LinkedIn.
     */
    LINKEDIN,

    /**
     * Candidate came from Indeed.
     */
    INDEED,

    /**
     * Candidate was referred by another person.
     */
    REFERRAL,

    /**
     * Candidate was sourced directly by a recruiter.
     */
    DIRECT_SOURCING,

    /**
     * Candidate already existed in the recruitment database.
     */
    INTERNAL_DATABASE,

    /**
     * Candidate was provided by another recruitment agency.
     */
    RECRUITMENT_AGENCY,

    /**
     * Candidate contacted the agency directly,
     * without applying through a published job posting.
     */
    DIRECT_APPLICATION,

    /**
     * Facebook campaign
     */
    FACEBOOK,

    /**
     * Source is known but does not match a predefined value.
     */
    OTHER,

    ///
    DIRECT
}
