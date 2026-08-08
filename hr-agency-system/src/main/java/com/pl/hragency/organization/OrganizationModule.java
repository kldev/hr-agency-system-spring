package com.pl.hragency.organization;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(
        displayName = "Organization", allowedDependencies = { "shared :: event", "identity :: api" })
public class OrganizationModule {
}
