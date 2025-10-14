package com.lms.domain.course.spec.creation;

import java.util.List;

public record CreateSection(
    Integer seq,
    String name,
    List<CreateContent> contents
) {}
