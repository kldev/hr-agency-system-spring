package com.pl.hragency.recruitment.adapter.persistence.tag;
import com.pl.hragency.recruitment.application.port.TagQueryRepository;
import com.pl.hragency.recruitment.application.query.TagItem;
import com.pl.hragency.recruitment.api.TagCategory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TagQueryRepositoryIml implements TagQueryRepository {
    private final SpringDataTagRepository repository;

    public TagQueryRepositoryIml(SpringDataTagRepository repository) {
        this.repository = repository;
    }

    public static TagItem from(TagJpaEntity tag) {
        return new TagItem(tag.getId(), tag.getCode(), tag.getName(), tag.getCategory());
    }

    @Override
    public List<TagItem> getTags(String searchTag, TagCategory category) {
        var specification = Specification.allOf(
                TagSpecifications.active(),
                TagSpecifications.search(searchTag),
                TagSpecifications.category(category)
        );

        return repository.findAll(specification).stream()
                .map(TagQueryRepositoryIml::from).toList();

    }

    @Override
    public Optional<TagItem> getTagById(UUID id) {
        return repository.findById(id).map(TagQueryRepositoryIml::from);
    }


}
