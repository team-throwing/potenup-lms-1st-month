package com.lms.domain.course.spec.creation;

public record CreateContent(
    String name,
    Integer seq,
    String body
) {}
