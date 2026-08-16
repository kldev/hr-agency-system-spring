package com.pl.hragency.recruitment.feeds.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.SalaryRange;
import com.pl.hragency.jobdescription.api.WorkMode;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record JobFeedItem(
        UUID id,
        String title,
        String summary,
        String description,

        @JacksonXmlElementWrapper(localName = "responsibilities")
        @JacksonXmlProperty(localName = "responsibility")
        List<String> responsibilities,

        @JacksonXmlElementWrapper(localName = "requirements")
        @JacksonXmlProperty(localName = "requirement")
        List<String> requirements,

        @JacksonXmlElementWrapper(localName = "skills")
        @JacksonXmlProperty(localName = "skill")
        List<String> skills,


        String location,
        String countryCode,
        EmploymentType employmentType,
        WorkMode workMode,
        SalaryRange salaryRange,
        Instant createdAt,
        Instant updatedAt
) {
}