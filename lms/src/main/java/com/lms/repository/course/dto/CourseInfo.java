package com.lms.repository.course.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CourseInfo (
        Integer id,
        String title,
        String summary,
        String detail,
        Integer subCategoryId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long userId,
        String userName,
        Long totalCount
) {
}
