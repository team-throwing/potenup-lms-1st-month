package com.lms.domain.course;

import com.lms.domain.course.spec.creation.CreateContent;
import com.lms.domain.course.spec.rebuild.RebuildContent;
import com.lms.util.Validation;
import lombok.Getter;

import java.util.Optional;

@Getter
public class Content {
    private Long id;
    private String name;
    private Integer seq;
    private String body;

    private Content(Long id, String name, Integer seq, String body) {
        validateSeq(seq);
        validateName(name);

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

    void nextSeq() {
        this.seq++;
    }

    void specifiedSeq(Integer specifiedSeq) {
        this.seq =  specifiedSeq;
    }

    private void validateName(String name) throws IllegalArgumentException {
        Optional.ofNullable(name).orElseThrow(() ->
            new IllegalArgumentException("컨텐츠의 이름이 비었습니다.")
        );
    }

    private void validateSeq(Integer seq) throws IllegalArgumentException {
        Validation.requirePositive(seq, "컨텐츠 순서는 음수가 될 수 없습니다.");

        Optional.ofNullable(seq).orElseThrow(() ->
            new IllegalArgumentException("컨텐츠 순서 번호가 알맞지 않습니다. 값을 확인해주세요.")
        );
    }
}
