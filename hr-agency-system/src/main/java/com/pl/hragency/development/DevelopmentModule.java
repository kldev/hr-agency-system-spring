package com.pl.hragency.development;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(
        displayName = "Development", allowedDependencies = {
                "organization :: api",
                "identity :: api",
                "company :: api",
                "sales :: api",
                "jobdescription :: api"
        })
public class DevelopmentModule {
}
