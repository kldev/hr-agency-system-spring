package com.pl.hragency.jobdescription.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class JobDescription {

    private final JobDescriptionId id;
    private final UUID organizationId;
    private final UUID companyId;

    private String title;
    private String summary;
    private String description;

    private List<String> responsibilities;
    private List<String> requirements;
    private List<String> skills;

    private String location;
    private String countryCode;

    private EmploymentType employmentType;
    private WorkMode workMode;
    private SalaryRange salaryRange;

    private JobDescriptionStatus status;

    private final UUID recruiterId;

    private final Instant createdAt;
    private Instant updatedAt;

    private JobDescription(
            JobDescriptionId id,
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
            EmploymentType employmentType,
            WorkMode workMode,
            SalaryRange salaryRange,
            JobDescriptionStatus status,
            UUID recruiterId,
            Instant createdAt,
            Instant updatedAt) {

        this.id = id;
        this.organizationId = organizationId;
        this.companyId = companyId;
        this.title = title;
        this.summary = summary;
        this.description = description;
        this.responsibilities = List.copyOf(responsibilities);
        this.requirements = List.copyOf(requirements);
        this.skills = List.copyOf(skills);
        this.location = location;
        this.countryCode = countryCode;
        this.employmentType = employmentType;
        this.workMode = workMode;
        this.salaryRange = salaryRange;
        this.status = status;
        this.recruiterId = recruiterId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static JobDescription create(
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
            EmploymentType employmentType,
            WorkMode workMode,
            SalaryRange salaryRange,
            UUID recruiterId) {

        var now = Instant.now();

        return new JobDescription(
                new JobDescriptionId(UUID.randomUUID()),
                organizationId,
                companyId,
                title,
                summary,
                description,
                responsibilities,
                requirements,
                skills,
                location,
                countryCode,
                employmentType,
                workMode,
                salaryRange,
                JobDescriptionStatus.DRAFT,
                recruiterId,
                now,
                now
        );
    }

    public static JobDescription rehydrate(
            JobDescriptionId id,
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
            EmploymentType employmentType,
            WorkMode workMode,
            SalaryRange salaryRange,
            JobDescriptionStatus status,
            UUID recruiterId,
            Instant createdAt,
            Instant updatedAt) {

        return new JobDescription(
                id,
                organizationId,
                companyId,
                title,
                summary,
                description,
                responsibilities,
                requirements,
                skills,
                location,
                countryCode,
                employmentType,
                workMode,
                salaryRange,
                status,
                recruiterId,
                createdAt,
                updatedAt
        );
    }

    public void update(
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
            SalaryRange salaryRange) {

        if (status == JobDescriptionStatus.CLOSED ||
                status == JobDescriptionStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Closed job description cannot be modified"
            );
        }

        this.title = title;
        this.summary = summary;
        this.description = description;
        this.responsibilities = List.copyOf(responsibilities);
        this.requirements = List.copyOf(requirements);
        this.skills = List.copyOf(skills);
        this.location = location;
        this.countryCode = countryCode;
        this.employmentType = employmentType;
        this.workMode = workMode;
        this.salaryRange = salaryRange;
        this.updatedAt = Instant.now();
    }

    public void open() {

        if (status != JobDescriptionStatus.DRAFT &&
                status != JobDescriptionStatus.ON_HOLD) {

            throw new IllegalStateException(
                    "Job description cannot be opened from status %s"
                            .formatted(status)
            );
        }

        status = JobDescriptionStatus.OPEN;
        updatedAt = Instant.now();
    }

    public void putOnHold() {

        if (status != JobDescriptionStatus.OPEN) {
            throw new IllegalStateException(
                    "Only open job description can be put on hold"
            );
        }

        status = JobDescriptionStatus.ON_HOLD;
        updatedAt = Instant.now();
    }

    public void close() {

        if (status == JobDescriptionStatus.CLOSED) {
            return;
        }

        status = JobDescriptionStatus.CLOSED;
        updatedAt = Instant.now();
    }

    public void cancel() {

        if (status == JobDescriptionStatus.CLOSED) {
            throw new IllegalStateException(
                    "Closed job description cannot be cancelled"
            );
        }

        status = JobDescriptionStatus.CANCELLED;
        updatedAt = Instant.now();
    }

    public JobDescriptionId id() {
        return id;
    }

    public UUID organizationId() {
        return organizationId;
    }

    public UUID companyId() {
        return companyId;
    }

    public String title() {
        return title;
    }

    public String summary() {
        return summary;
    }

    public String description() {
        return description;
    }

    public List<String> responsibilities() {
        return responsibilities;
    }

    public List<String> requirements() {
        return requirements;
    }

    public List<String> skills() {
        return skills;
    }

    public String location() {
        return location;
    }

    public String countryCode() {
        return countryCode;
    }

    public EmploymentType employmentType() {
        return employmentType;
    }

    public WorkMode workMode() {
        return workMode;
    }

    public SalaryRange salaryRange() {
        return salaryRange;
    }

    public JobDescriptionStatus status() {
        return status;
    }

    public UUID recruiterId() {
        return recruiterId;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}