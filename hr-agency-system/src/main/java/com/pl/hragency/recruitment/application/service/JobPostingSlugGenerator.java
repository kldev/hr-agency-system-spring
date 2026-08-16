package com.pl.hragency.recruitment.application.service;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JobPostingSlugGenerator {

    public String generate(
            String companyName,
            String title,
            String location,
            UUID postingId
    ) {
        String base = Stream.of(companyName, title, location)
                .filter(Objects::nonNull)
                .filter(value -> !value.isBlank())
                .map(this::slugify)
                .collect(Collectors.joining("-"));

        String suffix = postingId
                .toString()
                .replace("-", "")
                .substring(0, 4);

        return base + "-" + suffix;
    }

    private String slugify(String value) {
        return Normalizer.normalize(
                        value.trim().toLowerCase(Locale.ROOT),
                        Normalizer.Form.NFD
                )
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}