package com.pl.hragency.recruitment.application.port;

import com.pl.hragency.recruitment.application.query.TagItem;
import com.pl.hragency.recruitment.api.TagCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagQueryRepository {
    List<TagItem> getTags(String searchTag, TagCategory category);
    Optional<TagItem> getTagById(UUID id);
}
