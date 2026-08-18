package com.pl.hragency.recruitment.api;

import java.util.List;

public interface RecruitmentApi {
    List<TagSuggestion> findTagsSuggestions(String search, TagCategory category);
}
