package com.pl.hragency.recruitment.application.query;

import java.time.Instant;
import java.util.UUID;

public record CandidateItem(UUID id,
                            String email,
                            String firstName,
                            String lastName,
                            String summary,
                            String phone,
                            Instant createdAt) {
}
