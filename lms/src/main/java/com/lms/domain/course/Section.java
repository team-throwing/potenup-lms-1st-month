package com.lms.domain.course;

import com.lms.domain.course.spec.creation.CreateSection;
import com.lms.domain.course.spec.rebuild.RebuildSection;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
public class Section {
    private Integer id;
    private String name;
    private Integer seq;
    private List<Content> contents;

    private Section(
        Integer id, String name, Integer seq, List<Content> contents
    ) throws IllegalArgumentException {
        validateName(name);
        validateSeq(seq);

        this.id = id;
        this.name = name;
        this.seq = seq;
        this.contents = contents;
    }

    static Section create(CreateSection createSection) throws IllegalArgumentException {
        List<Content> initialContent = Optional.ofNullable(createSection.contents())
            .orElse(List.of())
            .stream().map(Content::create).toList();

        return new Section(
            null,
            createSection.name(),
            createSection.seq(),
            initialContent
        );
    }

    static Section rebuild(RebuildSection rebuildSection) throws IllegalArgumentException {
        List<Content> initialContent = Optional.ofNullable(rebuildSection.contents())
            .orElse(List.of())
            .stream().map(Content::rebuild).toList();

        return new Section(rebuildSection.id(), rebuildSection.name(), rebuildSection.seq(), initialContent);
    }

    private void validateName(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("섹션의 이름이 비었습니다.");
        }
    }

    private void validateSeq(Integer seq) throws IllegalArgumentException {
        if (seq == null || seq < 1) {
            throw new IllegalArgumentException("섹션의 순서 번호가 알맞지 않습니다. 값을 확인햊주세요.");
        }
    }
}
