package com.pl.hragency.recruitment.api;



import java.util.List;
import java.util.Set;
import java.util.UUID;

public record TagSuggestion(UUID id, TagCategory category, String code, String name) {

}
