package com.lms.domain.course.spec;

import java.util.List;

public record CreateCourse(
    String title,
    String summary,
    String detail,
    List<CreateSection> sections
) {}
