package com.pl.hragency.recruitment.application.command;

import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.WorkMode;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateJobPostingCommand(
    UUID jobDescriptionId,

    UUID recruitmentId,

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

    String salaryCurrency)
{
}
