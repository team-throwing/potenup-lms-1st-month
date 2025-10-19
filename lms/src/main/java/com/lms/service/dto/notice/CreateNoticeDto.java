package com.lms.service.dto.notice;

import com.lms.domain.course.Notice;

public record CreateNoticeDto(
    String body,
    Integer courseId
) {
    public Notice toEntity() {
        return Notice.create(body, courseId);
    }
}
