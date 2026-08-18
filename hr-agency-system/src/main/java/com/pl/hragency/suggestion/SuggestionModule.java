package com.pl.hragency.suggestion;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(
        displayName = "Suggestion", allowedDependencies = {
                "identity :: api", "company :: api", "shared :: rest",
                "recruitment :: api"
        })
public class SuggestionModule {
}
