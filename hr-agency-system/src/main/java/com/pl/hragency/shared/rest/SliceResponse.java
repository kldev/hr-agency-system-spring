package com.pl.hragency.shared.rest;

import org.springframework.data.domain.Slice;

import java.util.List;

public record SliceResponse<T>(
        List<T> content,
        boolean hasNext
) {
    public static <T> SliceResponse<T> from(
            Slice<T> page
    ) {
        return new SliceResponse<T>(page.getContent(), page.hasNext());
    }
}
