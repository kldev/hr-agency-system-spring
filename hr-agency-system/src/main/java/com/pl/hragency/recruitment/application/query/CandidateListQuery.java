package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.recruitment.domain.model.candidate.CandidateStatus;

import java.util.Set;
import java.util.UUID;

public record CandidateListQuery(String search, UUID companyId, Set<UUID> tags, CandidateStatus status) {
}
