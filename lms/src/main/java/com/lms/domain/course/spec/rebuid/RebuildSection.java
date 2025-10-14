package com.lms.domain.course.spec.rebuid;

import java.util.List;

public record RebuildSection(
    Integer id,
    Integer seq,
    String name,
    List<RebuildContent> contents
) {
}
