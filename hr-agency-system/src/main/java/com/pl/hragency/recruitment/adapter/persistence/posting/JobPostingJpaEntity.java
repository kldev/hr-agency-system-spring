package com.pl.hragency.recruitment.adapter.persistence.posting;

import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.SalaryRange;
import com.pl.hragency.jobdescription.api.WorkMode;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_postings")
public class JobPostingJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "organization_id", nullable = false, updatable = false)
    private UUID organizationId;

    @Column(name = "job_description_id", nullable = false, updatable = false)
    private UUID jobDescriptionId;

    @Column(name = "company_id", nullable = false, updatable = false)
    private UUID companyId;

    @Column(name = "recruiter_id", nullable = false, updatable = false)
    private UUID recruiterId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "summary", length = 1000)
    private String summary;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "responsibilities", nullable = false, columnDefinition = "jsonb")
    private List<String> responsibilities = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "requirements", nullable = false, columnDefinition = "jsonb")
    private List<String> requirements = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "skills", nullable = false, columnDefinition = "jsonb")
    private List<String> skills = new ArrayList<>();

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "country_code", length = 10)
    private String countryCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false, length = 30)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_mode", nullable = false, length = 30)
    private WorkMode workMode;

    @Column(name = "salary_min", precision = 12, scale = 2)
    private BigDecimal salaryMin;

    @Column(name = "salary_max", precision = 12, scale = 2)
    private BigDecimal salaryMax;

    @Column(name = "salary_currency", length = 3)
    private String salaryCurrency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private JobPostingStatus status;

    @Column(name = "slug", length = 500)
    private String slug;

    @Column(name = "organization_slug", length = 200)
    private String organizationSlug;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    protected JobPostingJpaEntity() {
        // JPA
    }

    public JobPostingJpaEntity(
            UUID id,
            UUID organizationId,
            UUID jobDescriptionId,
            UUID companyId,
            UUID recruiterId,
            String organizationSlug,
            Instant createdAt,
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
            String slug)
    {
        this.id = id;
        this.organizationId = organizationId;
        this.jobDescriptionId = jobDescriptionId;
        this.companyId = companyId;
        this.recruiterId = recruiterId;
        this.createdAt = createdAt;
        this.organizationSlug = organizationSlug;
        this.title = title;
        this.summary = summary;
        this.description = description;
        this.responsibilities = new ArrayList<>(responsibilities);
        this.requirements = new ArrayList<>(requirements);
        this.skills = new ArrayList<>(skills);
        this.location = location;
        this.countryCode = countryCode;
        this.employmentType = employmentType;
        this.workMode = workMode;
        this.salaryMin = salaryRange.min();
        this.salaryMax = salaryRange.max();
        this.salaryCurrency = salaryRange.currency().getCurrencyCode();
        this.status = status;
        this.slug = slug;
        this.version = null;
    }

    public void update(
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
            String slug) {

        this.recruiterId = recruiterId;
        this.title = title;
        this.summary = summary;
        this.description = description;
        this.responsibilities = new ArrayList<>(responsibilities);
        this.requirements = new ArrayList<>(requirements);
        this.skills = new ArrayList<>(skills);
        this.location = location;
        this.countryCode = countryCode;
        this.employmentType = employmentType;
        this.workMode = workMode;
        this.salaryMin = salaryRange.min();
        this.salaryMax = salaryRange.max();
        this.salaryCurrency = salaryRange.currency().getCurrencyCode();
        this.status = status;
        this.slug = slug;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public UUID getJobDescriptionId() {
        return jobDescriptionId;
    }

    public UUID getCompanyId() { return companyId; }

    public UUID getRecruiterId() {
        return recruiterId;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getResponsibilities() {
        return responsibilities;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public List<String> getSkills() {
        return skills;
    }

    public String getLocation() {
        return location;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public WorkMode getWorkMode() {
        return workMode;
    }

    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    public String getSalaryCurrency() {
        return salaryCurrency;
    }

    public JobPostingStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    public String getSlug() {
        return slug;
    }

    public String getOrganizationSlug() {
        return organizationSlug;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}