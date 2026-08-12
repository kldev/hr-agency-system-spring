package com.pl.hragency.recruitment;

import org.springframework.modulith.ApplicationModule;

@ApplicationModule(
        displayName = "Recruitment", allowedDependencies = {
                "shared :: event", "identity :: api", "shared :: rest",
                "jobdescription :: api"})
public class RecruitmentModule {
}
