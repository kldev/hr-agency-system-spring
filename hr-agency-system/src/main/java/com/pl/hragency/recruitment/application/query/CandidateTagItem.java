package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.recruitment.api.TagCategory;

import java.util.UUID;

public record CandidateTagItem(UUID tagId,
                               String name,
                               TagCategory category) {
}
