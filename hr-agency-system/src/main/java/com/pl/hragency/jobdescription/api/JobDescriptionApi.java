package com.pl.hragency.jobdescription.api;

import java.util.UUID;

public interface JobDescriptionApi
{
        UUID create(UUID organizationId, UUID userId, CreateJobDescriptionInput request);
}
