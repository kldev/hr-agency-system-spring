package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public record JobPostingListQuery(String search, JobPostingStatus status, UUID companyId) {
}
