package com.lms.domain.course.spec;

import java.util.List;

public record CreateSection(
    String name,
    Integer seq,
    List<CreateContent> contents
) {}
