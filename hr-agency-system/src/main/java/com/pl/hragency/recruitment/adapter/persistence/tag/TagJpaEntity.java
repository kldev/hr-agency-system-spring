package com.pl.hragency.recruitment.adapter.persistence.tag;


import com.pl.hragency.recruitment.api.TagCategory;
import jakarta.persistence.*;

import java.util.UUID;

@Table(name = "tags")
@Entity
public class TagJpaEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "category",
            nullable = false,
            length = 50
    )
    private TagCategory category;

    @Column(name = "code", length =  100)
    private String code;

    @Column(name = "name", length =  255)
    private String name;

    @Column(name = "active")
    private boolean active;

    protected TagJpaEntity() {}

    public UUID getId() {
        return id;
    }

    public TagCategory getCategory() {
        return category;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }
}
