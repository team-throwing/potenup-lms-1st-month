package com.lms.domain.category;

import lombok.Getter;

@Getter
public class Category {
    private Integer id;
    private String name;
    private CategoryLevel level;
    private Integer parentId;

    public Category(
            Integer id,
            String name,
            CategoryLevel level,
            Integer parentId
    ) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.parentId = parentId;
    }

    private Category create(
        String name, CategoryLevel level, Integer parentId
    ) {
        return new Category(null, name, level, parentId);
    }

    private Category rebuild(
        Integer id, String name, CategoryLevel level, Integer parentId
    ) {
        return new Category(id, name, level, parentId);
    }
}
