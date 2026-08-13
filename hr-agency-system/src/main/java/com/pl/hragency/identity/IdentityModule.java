package com.pl.hragency.identity;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(
        displayName = "Identity", allowedDependencies = {
                "organization :: api",
                "shared :: event", "shared :: rest",
        "persistence"
        })
public class IdentityModule {
}
