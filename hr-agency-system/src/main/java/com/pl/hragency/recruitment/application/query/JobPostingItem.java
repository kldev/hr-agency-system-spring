package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.recruitment.domain.model.posting.JobPosting;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record JobPostingItem(
        UUID id,
        UUID organizationId,
        UUID jobDescriptionId,

        String title,
        String summary,
        String description,

        List<String> responsibilities,
        List<String> requirements,
        List<String> skills,

        String location,
        String countryCode,

        String employmentType,
        String workMode,

        BigDecimal salaryMin,
        BigDecimal salaryMax,
        String salaryCurrency
) {

    public static JobPostingItem from(
            JobPosting jobDescription
    ) {
        return new JobPostingItem(
                jobDescription.id().value(),
                jobDescription.organizationId(),
                jobDescription.jobDescriptionId(),

                jobDescription.title(),
                jobDescription.summary(),
                jobDescription.description(),

                List.copyOf(jobDescription.responsibilities()),
                List.copyOf(jobDescription.requirements()),
                List.copyOf(jobDescription.skills()),

                jobDescription.location(),
                jobDescription.countryCode(),

                jobDescription.employmentType().name(),
                jobDescription.workMode().name(),

                jobDescription.salaryRange().min(),
                jobDescription.salaryRange().max(),
                jobDescription.salaryRange().currency().getCurrencyCode()
        );
    }
}