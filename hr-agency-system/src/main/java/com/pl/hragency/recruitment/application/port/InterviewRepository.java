package com.pl.hragency.recruitment.application.port;

import com.pl.hragency.recruitment.domain.model.interview.Interview;
import com.pl.hragency.recruitment.domain.model.interview.InterviewId;

import java.util.Optional;
import java.util.UUID;

public interface InterviewRepository {
    void save(Interview interview);
    Optional<Interview> findById(UUID organizationId, InterviewId id);
}
