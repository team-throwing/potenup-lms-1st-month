package com.lms.service.dto.category;

import com.lms.domain.category.CategoryLevel;

public record UpdateCategoryDto(
    Integer id,
    String name,
    CategoryLevel level,
    Integer parentId
) {
}
