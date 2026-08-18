package com.pl.hragency.recruitment.adapter.persistence.tag;

import com.pl.hragency.recruitment.api.TagCategory;
import org.springframework.data.jpa.domain.Specification;

public final class TagSpecifications {

    private TagSpecifications() {
    }

    public static Specification<TagJpaEntity> search(String search) {
        if (search == null || search.isBlank()) {
            return null;
        }

        String pattern = "%" + search.trim().toLowerCase() + "%";

        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), pattern),
                cb.like(cb.lower(root.get("code")), pattern)
        );
    }

    public static Specification<TagJpaEntity> category(TagCategory category) {
        if (category == null) {
            return null;
        }

        return (root, query, cb) ->
                cb.equal(root.get("category"), category);
    }

    public static Specification<TagJpaEntity> active() {
        return (root, query, cb) ->
                cb.isTrue(root.get("active"));
    }
}
