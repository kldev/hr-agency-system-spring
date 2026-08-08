package com.pl.hragency.jobdescription.adapter.persistence;

import com.pl.hragency.jobdescription.domain.model.JobDescription;
import com.pl.hragency.jobdescription.domain.model.JobDescriptionId;
import com.pl.hragency.jobdescription.domain.model.SalaryRange;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.Currency;

@Component
public class JobDescriptionMapper {
    private final JsonMapper jsonMapper = new JsonMapper();
    public JobDescriptionJpaEntity toEntity(JobDescription jobDescription) {

        return new JobDescriptionJpaEntity(
                jobDescription.id().value(),
                jobDescription.organizationId(),
                jobDescription.companyId(),
                jobDescription.title(),
                jobDescription.summary(),
                jobDescription.status(),
                jobDescription.location(),
                jobDescription.countryCode(),
                jobDescription.workMode(),
                jobDescription.employmentType(),
                jobDescription.description(),
                jsonMapper.writeValueAsString(jobDescription.requirements()),
                jsonMapper.writeValueAsString(jobDescription.responsibilities()),
                jsonMapper.writeValueAsString(jobDescription.skills()),
                jobDescription.recruiterId(),
                jobDescription.salaryRange().min(),
                jobDescription.salaryRange().max(),
                jobDescription.salaryRange().currency().getCurrencyCode(),
                jobDescription.createdAt(),
                jobDescription.updatedAt()
        );
    }

    public JobDescription toDomain(JobDescriptionJpaEntity entity) {

        return JobDescription.rehydrate(
                new JobDescriptionId(entity.getId()),
                entity.getOrganizationId(),
                entity.getCompanyId(),
                entity.getTitle(),
                entity.getSummary(),
                entity.getDescription(),
                jsonMapper.readValue(entity.getResponsibilities(), new TypeReference<>() {}),
                jsonMapper.readValue(entity.getRequirements(), new TypeReference<>() {}),
                jsonMapper.readValue(entity.getSkills(), new TypeReference<>() {}),
                entity.getLocation(),
                entity.getCountryCode(),
                entity.getEmploymentType(),
                entity.getWorkMode(),
                new SalaryRange(entity.getSalaryMin(), entity.getSalaryMax(), Currency.getInstance(entity.getSalaryCurrency())),
                entity.getStatus(),
                entity.getRecruiterId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
