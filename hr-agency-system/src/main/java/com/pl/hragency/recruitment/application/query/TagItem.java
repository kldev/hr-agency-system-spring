package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.recruitment.api.TagCategory;

import java.util.UUID;

public record TagItem(UUID id, String code, String name, TagCategory category) {
}
