package com.lms.domain.course;

import com.lms.domain.course.spec.CreateContent;
import lombok.Getter;

@Getter
public class Content {
    private Long id;
    private String name;
    private Integer seq;
    private String body;

    private Content(Long id, String name, Integer seq, String body) {
        this.id = id;
        this.name = name;
        this.seq = seq;
        this.body = body;
    }

    public static Content create(CreateContent createContent) {
        return new Content(
            null,
            createContent.name(),
            createContent.seq(),
            createContent.body()
        );
    }

    public static Content rebuild(Long id, String name, Integer seq, String body) {
        return new Content(id, name, seq, body);
    }
}
