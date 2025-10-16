package com.lms.domain.course.spec.creation;

import java.util.List;

public record CreateCourse(
    String title,
    String summary,
    String detail,
    Integer subCategoryId,
    Long userId,
    List<CreateSection> sections
) {}
