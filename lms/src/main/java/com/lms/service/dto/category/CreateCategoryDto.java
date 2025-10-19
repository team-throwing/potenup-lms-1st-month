package com.lms.service.dto.category;

import com.lms.domain.category.Category;
import com.lms.domain.category.CategoryLevel;

public record CreateCategoryDto(
    String name,
    CategoryLevel level,
    Integer parentId
) {
    public Category toEntity() {
        return Category.create(name, level, parentId);
    }
}
