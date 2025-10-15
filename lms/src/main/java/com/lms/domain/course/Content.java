package com.lms.domain.course;

import com.lms.domain.course.spec.creation.CreateContent;
import com.lms.domain.course.spec.rebuild.RebuildContent;
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

    static Content create(CreateContent createContent) {
        return new Content(
            null,
            createContent.name(),
            createContent.seq(),
            createContent.body()
        );
    }

    static Content rebuild(RebuildContent rebuildContent) {
        return new Content(
            rebuildContent.id(),
            rebuildContent.name(),
            rebuildContent.seq(),
            rebuildContent.body()
        );
    }
}
