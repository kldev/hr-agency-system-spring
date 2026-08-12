package com.pl.hragency.recruitment.domain.model.posting;

public enum JobPostingStatus {
    /**
     * Posting is being prepared and is not publicly visible.
     */
    DRAFT,

    /**
     * Posting is currently active and available to candidates.
     */
    PUBLISHED,

    /**
     * Posting is no longer accepting new applications.
     */
    CLOSED,

    /**
     * Posting has been permanently archived.
     */
    ARCHIVED
}
