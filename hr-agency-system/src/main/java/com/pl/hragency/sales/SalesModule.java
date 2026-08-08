package com.pl.hragency.sales;


import org.springframework.modulith.ApplicationModule;

@ApplicationModule(
        displayName = "Sales", allowedDependencies = { "shared :: event", "identity :: api", "shared :: rest", "company :: api" })
public class SalesModule {
}
