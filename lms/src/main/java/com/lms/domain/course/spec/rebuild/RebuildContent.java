package com.lms.domain.course.spec.rebuild;

public record RebuildContent(
    Long id,
    String name,
    Integer seq,
    String body
) {}
