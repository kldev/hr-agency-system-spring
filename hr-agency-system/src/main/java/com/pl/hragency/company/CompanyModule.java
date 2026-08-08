package com.pl.hragency.company;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(
        displayName = "Company", allowedDependencies = {
                "shared :: event", "identity :: api", "shared :: rest",
                "shared :: persistance"
        })
public class CompanyModule {
}
