package com.pl.hragency.identity.adapter.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserQueryRepository {

    List<UserSuggestionProjection> suggestions(
            Specification<UserJpaEntity> specification,
            Pageable pageable
    );
}
