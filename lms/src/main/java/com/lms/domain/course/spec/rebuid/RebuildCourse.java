package com.lms.domain.course.spec.rebuid;

import com.lms.domain.course.spec.creation.CreateSection;

import java.time.LocalDateTime;
import java.util.List;

public record RebuildCourse(
    Integer id,
    String title,
    String summary,
    String detail,
    Integer subCategoryId,
    List<CreateSection> sections,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
