package com.lms.domain.course.spec.rebuild;

import java.util.List;

public record RebuildSection(
    Integer id,
    Integer seq,
    String name,
    List<RebuildContent> contents
) {
}
