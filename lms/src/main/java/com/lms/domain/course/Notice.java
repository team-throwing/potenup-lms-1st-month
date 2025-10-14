package com.lms.domain.course;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Notice {
    private Long id;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Notice(Long id, String body) {
        this.id = id;
        this.body = body;
        this.createdAt = LocalDateTime.now();
    }

    public Notice create(String body) {
        return new Notice(null, body);
    }

    public Notice rebuild(Long id, String body) {
        return new Notice(id, body);
    }
}
