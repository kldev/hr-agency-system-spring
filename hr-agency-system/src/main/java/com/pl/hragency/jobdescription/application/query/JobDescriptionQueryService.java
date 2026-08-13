package com.pl.hragency.jobdescription.application.query;

import com.pl.hragency.jobdescription.application.port.JobDescriptionRepository;

import com.pl.hragency.jobdescription.domain.model.JobDescriptionId;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class JobDescriptionQueryService {

    private final JobDescriptionRepository repository;

    public JobDescriptionQueryService(
            JobDescriptionRepository repository
    ) {
        this.repository = repository;
    }

    public JobDescriptionItem get(
            UUID organizationId,
            JobDescriptionId jobDescriptionId
    ) {
        return repository
                .findById(organizationId, jobDescriptionId)
                .map(JobDescriptionItem::from)
                .orElseThrow(() ->
                        new EntityNotFoundException(EntityType.JobDescription, jobDescriptionId.value())
                );
    }

    public List<JobDescriptionItem> findByCompany(
            UUID organizationId,
            UUID companyId
    ) {
        return repository
                .findByCompanyId(organizationId, companyId)
                .stream()
                .map(JobDescriptionItem::from)
                .toList();
    }

    public Page<JobDescriptionItem> search(
            UUID organizationId,
            JobDescriptionListQuery query
    ) {
        return repository
                .search(organizationId, query).map(JobDescriptionItem::from);

    }
}