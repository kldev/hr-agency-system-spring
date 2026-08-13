package com.pl.hragency.recruitment.application.command;

import com.pl.hragency.recruitment.domain.model.application.JobApplicationStatus;

public record ChangeJobApplicationStatusCommand(JobApplicationStatus status) {
}
