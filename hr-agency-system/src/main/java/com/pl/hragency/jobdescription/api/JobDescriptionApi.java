package com.pl.hragency.jobdescription.api;

import java.util.Optional;
import java.util.UUID;

public interface JobDescriptionApi
{
        UUID create(UUID organizationId, UUID userId, CreateJobDescriptionInput request);
        boolean exists(UUID organizationId, UUID id);
        Optional<JobDescriptionBase> get(UUID organizationId, UUID id);
}
