package com.lms.domain.course.spec;

public record CreateContent(
    String name,
    Integer seq,
    String body
) {}
