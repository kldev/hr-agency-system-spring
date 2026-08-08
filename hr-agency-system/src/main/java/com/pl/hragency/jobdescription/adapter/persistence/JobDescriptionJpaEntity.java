package com.pl.hragency.jobdescription.adapter.persistence;

import com.pl.hragency.jobdescription.domain.model.EmploymentType;
import com.pl.hragency.jobdescription.domain.model.JobDescriptionStatus;
import com.pl.hragency.jobdescription.domain.model.WorkMode;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "job_descriptions",
        indexes = {
                @Index(
                        name = "idx_job_descriptions_organization",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_job_descriptions_organization_company",
                        columnList = "organization_id, company_id"
                ),
                @Index(
                        name = "idx_job_descriptions_organization_status",
                        columnList = "organization_id, status"
                )
        }
)
public class JobDescriptionJpaEntity {

    @Id
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "recruiter_id", nullable = false)
    private UUID recruiterId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "summary", length = 1000)
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private JobDescriptionStatus status;

    @Column(name = "location", length = 150)
    private String location;

    @Column(name = "country_code", length = 10)
    private String countryCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", length = 10)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_mode", length = 30)
    private WorkMode workMode;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "requirements", columnDefinition = "jsonb")
    private String requirements;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "responsibilities", columnDefinition = "jsonb")
    private String responsibilities;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "skills", columnDefinition = "jsonb")
    private String skills;

    @Column(name = "salary_min", scale = 2, precision = 12)
    private BigDecimal salaryMin;

    @Column(name = "salary_max", scale = 2, precision = 12)
    private BigDecimal salaryMax;

    @Column(name = "salary_currency", length = 3)
    private String salaryCurrency;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected JobDescriptionJpaEntity() {
    }

    public JobDescriptionJpaEntity(
            UUID id,
            UUID organizationId,
            UUID companyId,
            String title,
            String summary,
            JobDescriptionStatus status,
            String location,
            String countryCode,
            WorkMode workMode,
            EmploymentType employmentType,
            String description,
            String requirements,
            String responsibilities,
            String skills,
            UUID recruiterId,
            BigDecimal salaryMin,
            BigDecimal salaryMax,
            String salaryCurrency,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.organizationId = organizationId;
        this.companyId = companyId;
        this.title = title;
        this.summary = summary;
        this.status = status;
        this.location = location;
        this.countryCode = countryCode;
        this.workMode = workMode;
        this.employmentType = employmentType;
        this.description = description;
        this.requirements = requirements;
        this.responsibilities = responsibilities;
        this.skills = skills;
        this.recruiterId = recruiterId;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.salaryCurrency = salaryCurrency;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public JobDescriptionStatus getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public WorkMode getWorkMode() {
        return workMode;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public UUID getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(UUID recruiterId) {
        this.recruiterId = recruiterId;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(BigDecimal salaryMin) {
        this.salaryMin = salaryMin;
    }

    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(BigDecimal salaryMax) {
        this.salaryMax = salaryMax;
    }

    public String getSalaryCurrency() {
        return salaryCurrency;
    }

    public void setSalaryCurrency(String salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }
}
