package com.pl.hragency.recruitment.application.service;

import com.pl.hragency.recruitment.api.RecruitmentApi;
import com.pl.hragency.recruitment.api.TagCategory;
import com.pl.hragency.recruitment.api.TagSuggestion;
import com.pl.hragency.recruitment.application.port.TagQueryRepository;
import com.pl.hragency.recruitment.application.query.TagItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruitmentService implements RecruitmentApi {
    private final TagQueryRepository queryRepository;

    public RecruitmentService(TagQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    @Override
    public List<TagSuggestion> findTagsSuggestions(String search, TagCategory category) {
        return queryRepository.getTags(search, category)
                .stream().map(RecruitmentService::from).toList();
    }

    public static TagSuggestion from(TagItem tagItem) {
        return new TagSuggestion(tagItem.id(),
                tagItem.category(),
                tagItem.code(),
                tagItem.name());
    }
}
