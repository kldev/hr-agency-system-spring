package com.pl.hragency.jobdescription;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(
        displayName = "Job Description", allowedDependencies = { "shared :: event", "identity :: api", "shared :: rest", "company :: api" })
public class JobDescriptionModule {
}
