package com.pl.hragency.shared.rest;

public class EntityNotFoundException extends RuntimeException {

    private final EntityType entityType;
    private final Object id;

    public EntityNotFoundException(
            EntityType entityType,
            Object id) {

        super("%s with id %s was not found"
                .formatted(entityType, id));

        this.entityType = entityType;
        this.id = id;
    }

    public EntityType entityType() {
        return entityType;
    }

    public Object id() {
        return id;
    }
}
