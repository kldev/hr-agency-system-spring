package com.pl.hragency.suggestion;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(
        displayName = "Suggestion", allowedDependencies = {
                "identity :: api", "company :: api", "shared :: rest" })
public class SuggestionModule {
}
