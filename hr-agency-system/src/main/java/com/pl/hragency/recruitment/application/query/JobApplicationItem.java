package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;

import java.time.Instant;
import java.util.UUID;

public record JobApplicationItem(UUID id,
                                 UUID candidateId,
                                 String email,
                                 String firstName,
                                 String lastName,
                                 String phone,
                                 CandidateSource source,
                                 Instant createdAt,
                                 UUID recruiterId,
                                 String recruiterFullName,
                                 UUID companyId) {
}
