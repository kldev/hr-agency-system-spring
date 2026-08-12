package com.pl.hragency.jobdescription.application.command;

import com.pl.hragency.jobdescription.domain.model.JobDescriptionStatus;

public record ChangeJobDescriptionStatusCommand(JobDescriptionStatus status) {
}
