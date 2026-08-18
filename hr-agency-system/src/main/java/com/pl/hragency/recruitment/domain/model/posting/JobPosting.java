package com.pl.hragency.recruitment.domain.model.posting;

import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.SalaryRange;
import com.pl.hragency.jobdescription.api.WorkMode;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class JobPosting {

    private final JobPostingId id;
    private final UUID organizationId;
    private final UUID jobDescriptionId;
    private final UUID companyId;
    private UUID recruiterId;

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

    private JobPostingStatus status;
    private String slug;
    private String organizationSlug;

    private final Instant createdAt;

    private JobPosting(
            JobPostingId id,
            UUID organizationId,
            UUID jobDescriptionId,
            UUID companyId,
            UUID recruiterId,
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
            JobPostingStatus status,
            String slug,
            String organizationSlug,
            Instant createdAt) {

        this.id = Objects.requireNonNull(id);
        this.organizationId = Objects.requireNonNull(organizationId);
        this.jobDescriptionId = Objects.requireNonNull(jobDescriptionId);
        this.recruiterId = Objects.requireNonNull(recruiterId);
        this.companyId = Objects.requireNonNull(companyId);

        this.title = requireText(title, "Title");
        this.summary = summary;
        this.description = requireText(description, "Description");

        this.responsibilities = copy(responsibilities, "Responsibilities");
        this.requirements = copy(requirements, "Requirements");
        this.skills = copy(skills, "Skills");

        this.location = location;
        this.countryCode = countryCode;

        this.employmentType = Objects.requireNonNull(employmentType);
        this.workMode = Objects.requireNonNull(workMode);
        this.salaryRange = salaryRange;

        this.status = Objects.requireNonNull(status);
        this.slug = Objects.requireNonNull(slug);
        this.organizationSlug = Objects.requireNonNull(organizationSlug);

        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static JobPosting draft(
            UUID organizationId,
            UUID jobDescriptionId,
            UUID companyId,
            UUID recruiterId,
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
            String slug,
            String organizationSlug
            ) {

        Instant now = Instant.now();

        return new JobPosting(
                JobPostingId.newId(),
                organizationId,
                jobDescriptionId,
                companyId,
                recruiterId,
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
                JobPostingStatus.DRAFT,
                slug,
                organizationSlug,
                now
        );
    }

    public static JobPosting rehydrate(
            JobPostingId id,
            UUID organizationId,
            UUID jobDescriptionId,
            UUID companyId,
            UUID recruiterId,
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
            JobPostingStatus status,
            String slug,
            String organizationSlug,
            Instant createdAt) {

        return new JobPosting(
                id,
                organizationId,
                jobDescriptionId,
                companyId,
                recruiterId,
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
                slug,
                organizationSlug,
                createdAt
        );
    }

    public void publish() {
        if (status != JobPostingStatus.DRAFT) {
            throw new IllegalStateException(
                    "Only draft job posting can be published"
            );
        }

        status = JobPostingStatus.PUBLISHED;
    }

    public void close() {
        if (status != JobPostingStatus.PUBLISHED) {
            throw new IllegalStateException(
                    "Only published job posting can be closed"
            );
        }

        status = JobPostingStatus.CLOSED;
    }

    public void archive() {
        if (status != JobPostingStatus.CLOSED) {
            throw new IllegalStateException(
                    "Only closed job posting can be archived"
            );
        }

        status = JobPostingStatus.ARCHIVED;
    }

    public boolean active(){
        return status == JobPostingStatus.PUBLISHED;
    }

    public void updateContent(
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

        if (status != JobPostingStatus.DRAFT) {
            throw new IllegalStateException(
                    "Only draft job posting can be modified"
            );
        }

        this.title = requireText(title, "Title");
        this.summary = summary;
        this.description = requireText(description, "Description");

        this.responsibilities = copy(responsibilities, "Responsibilities");
        this.requirements = copy(requirements, "Requirements");
        this.skills = copy(skills, "Skills");

        this.location = location;
        this.countryCode = countryCode;

        this.employmentType = Objects.requireNonNull(employmentType);
        this.workMode = Objects.requireNonNull(workMode);
        this.salaryRange = salaryRange;
    }

    public void updateRecruiter(UUID recruiterId) {
        this.recruiterId = recruiterId;
    }

    public void updateStatus(JobPostingStatus status) {

        if (status == status()) {
            throw new IllegalStateException("Cannot change status for the same of job posting");
        }

        switch (status) {
            case PUBLISHED -> publish();
            case CLOSED -> close();
            case ARCHIVED -> archive();
        }

    }


    private static List<String> copy(
            List<String> values,
            String fieldName) {

        Objects.requireNonNull(values, fieldName + " must not be null");

        return List.copyOf(values);
    }

    private static String requireText(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " must not be blank"
            );
        }

        return value;
    }

    public JobPostingId id() {
        return id;
    }

    public UUID organizationId() {
        return organizationId;
    }

    public UUID jobDescriptionId() {
        return jobDescriptionId;
    }

    public UUID companyId() { return  companyId;}

    public UUID recruiterId() {
        return recruiterId;
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

    public JobPostingStatus status() {
        return status;
    }

    public String slug() { return slug; }

    public String organizationSlug() { return  organizationSlug; }

    public Instant createdAt() {
        return createdAt;
    }

}