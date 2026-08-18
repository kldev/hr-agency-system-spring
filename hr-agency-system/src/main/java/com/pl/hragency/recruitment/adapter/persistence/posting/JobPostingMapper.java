package com.pl.hragency.recruitment.adapter.persistence.posting;
import com.pl.hragency.jobdescription.api.SalaryRange;
import com.pl.hragency.recruitment.domain.model.posting.JobPosting;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import org.springframework.stereotype.Component;
import java.util.Currency;


@Component
public class JobPostingMapper {

    public void mapFields(JobPosting posting, JobPostingJpaEntity entity) {

        entity.update(
                posting.recruiterId(),
                posting.title(),
                posting.summary(),
                posting.description(),
                posting.responsibilities(),
                posting.requirements(),
                posting.skills(),
                posting.location(),
                posting.countryCode(),
                posting.employmentType(),
                posting.workMode(),
                posting.salaryRange(),
                posting.status(),
                posting.slug());
    }

    public JobPosting toDomain(JobPostingJpaEntity entity) {
        return JobPosting.rehydrate(
                new JobPostingId(entity.getId()),
                entity.getOrganizationId(),
                entity.getJobDescriptionId(),
                entity.getCompanyId(),
                entity.getRecruiterId(),
                entity.getTitle(),
                entity.getSummary(),
                entity.getDescription(),
                entity.getResponsibilities(),
                entity.getRequirements(),
                entity.getSkills(),
                entity.getLocation(),
                entity.getCountryCode(),
                entity.getEmploymentType(),
                entity.getWorkMode(),
                new SalaryRange(entity.getSalaryMin(), entity.getSalaryMax(),
                        Currency.getInstance(entity.getSalaryCurrency())),
                entity.getStatus(),
                entity.getSlug(),
                entity.getOrganizationSlug(),
                entity.getCreatedAt()
        );
    }
}
