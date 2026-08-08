package com.pl.hragency.jobdescription.application.query;

import com.pl.hragency.jobdescription.domain.model.JobDescription;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record JobDescriptionItem(
        UUID id,
        UUID organizationId,
        UUID companyId,

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

    public static JobDescriptionItem from(
            JobDescription jobDescription
    ) {
        return new JobDescriptionItem(
                jobDescription.id().value(),
                jobDescription.organizationId(),
                jobDescription.companyId(),

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