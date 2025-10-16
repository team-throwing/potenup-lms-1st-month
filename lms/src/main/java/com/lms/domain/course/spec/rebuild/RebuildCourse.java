package com.lms.domain.course.spec.rebuild;

import java.time.LocalDateTime;
import java.util.List;

public record RebuildCourse(
    Integer id,
    String title,
    String summary,
    String detail,
    Integer subCategoryId,
    Long userId,
    List<RebuildSection> sections,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
