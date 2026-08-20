package com.pl.hragency.recruitment.adapter.persistence.posting;

import com.pl.hragency.recruitment.application.port.JobPostingQueryRepository;
import com.pl.hragency.recruitment.application.query.JobPostingItem;
import com.pl.hragency.recruitment.application.query.JobPostingItemMapper;
import com.pl.hragency.recruitment.application.query.JobPostingListQuery;
import com.pl.hragency.shared.persistence.AbstractJpaSliceQueryRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JobPostingQueryRepositoryAdapter
    extends AbstractJpaSliceQueryRepository<JobPostingJpaEntity, JobPostingListQuery, JobPostingItem>
            implements JobPostingQueryRepository {

    private final String appUrl;

    public JobPostingQueryRepositoryAdapter(EntityManager entityManager, @Value("${app.base.url:http://localhost:8080}") String appUrl) {
        super(entityManager);
        this.appUrl = appUrl;
    }

    @Override
    protected Class<JobPostingJpaEntity> entityType() {
        return JobPostingJpaEntity.class;
    }

    @Override
    protected Specification<JobPostingJpaEntity> specification(UUID organizationId, JobPostingListQuery query) {
        return Specification.allOf(JobPostingSpecifications.organizationId(organizationId),
                JobPostingSpecifications.companyId(query.companyId()),
                JobPostingSpecifications.search(query.search()),
                JobPostingSpecifications.jobDescriptionId(query.jobDescriptionId()),
                JobPostingSpecifications.status(query.status()));
    }

    @Override
    protected JobPostingItem from(JobPostingJpaEntity entity) {
        return new JobPostingItem(entity.getId(),
                entity.getOrganizationId(),
                entity.getJobDescriptionId(),
                entity.getOrganizationSlug(),
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
                entity.getSalaryMin(),
                entity.getSalaryMax(),
                entity.getSalaryCurrency(),
                entity.getSlug(),
                appUrl + "/public/" + entity.getOrganizationSlug() + "/apply/" + entity.getSlug()
                );
    }
}
