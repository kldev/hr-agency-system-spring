package com.pl.hragency.audit;


import org.springframework.modulith.ApplicationModule;

@ApplicationModule(
        displayName = "Audit", allowedDependencies = {
                "identity :: api",
                "organization :: event",
                "identity :: event",
                "company :: event",
                "jobdescription :: event",
                "sales :: event",
                "recruitment :: event",
                "shared :: event"
        })
public class AuditModule {
}
