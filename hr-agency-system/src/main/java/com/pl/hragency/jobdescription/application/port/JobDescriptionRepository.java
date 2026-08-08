package com.pl.hragency.jobdescription.application.port;

import com.pl.hragency.jobdescription.application.query.JobDescriptionListQuery;
import com.pl.hragency.jobdescription.domain.model.JobDescription;
import com.pl.hragency.jobdescription.domain.model.JobDescriptionId;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobDescriptionRepository {
    void save(JobDescription jobDescription);

    Optional<JobDescription> findById(
            UUID organizationId,
            JobDescriptionId id
    );

    List<JobDescription> findByCompanyId(UUID organizationId, UUID companyId);

    Page<JobDescription> search(UUID organizationId, JobDescriptionListQuery query);
}