package com.pl.hragency.identity.application.query;


import org.springframework.data.domain.Pageable;

public record UserListQuery(
        String search,
        Pageable pageable
) {
}
