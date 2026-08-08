package com.pl.hragency.jobdescription.application.command;

import com.pl.hragency.jobdescription.domain.model.EmploymentType;
import com.pl.hragency.jobdescription.domain.model.WorkMode;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateJobDescriptionCommand(

        UUID companyId,

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

        String salaryCurrency

) {
}