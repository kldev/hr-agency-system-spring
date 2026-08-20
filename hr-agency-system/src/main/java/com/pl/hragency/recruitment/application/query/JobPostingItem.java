package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.WorkMode;
import com.pl.hragency.recruitment.domain.model.posting.JobPosting;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record JobPostingItem(
        UUID id,
        UUID organizationId,
        UUID jobDescriptionId,
        String organizationSlug,

        String title,
        String summary,
        String description,

        List<String> responsibilities,
        List<String> requirements,
        List<String> skills,

        String location,
        String countryCode,

        EmploymentType employmentType,
        WorkMode workMode,

        BigDecimal salaryMin,
        BigDecimal salaryMax,
        String salaryCurrency,
        String slug,
        String applyUrl
) {


}