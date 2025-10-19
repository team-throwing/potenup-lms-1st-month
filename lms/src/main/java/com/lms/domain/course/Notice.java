package com.lms.domain.course;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Notice {
    private Long id;
    private String body;
    private Integer courseId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Notice create(String body, Integer courseId) {
        return new Notice(null, body, courseId, LocalDateTime.now(), null);
    }

    public static Notice rebuild(
        Long id, String body, Integer courseId,
        LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new Notice(id, body, courseId, createdAt, updatedAt);
    }

    public void updateBody(String body) {
        this.body = body;
        this.updatedAt = LocalDateTime.now();
    }

    private Notice(Long id, String body, Integer courseId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.body = body;
        this.courseId = courseId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
