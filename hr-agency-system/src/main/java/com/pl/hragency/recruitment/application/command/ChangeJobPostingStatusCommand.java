package com.pl.hragency.recruitment.application.command;

import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;

public record ChangeJobPostingStatusCommand(JobPostingStatus status) {
}
