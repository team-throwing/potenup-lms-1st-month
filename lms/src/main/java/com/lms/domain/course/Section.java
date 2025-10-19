package com.lms.domain.course;

import com.lms.domain.course.spec.creation.CreateSection;
import com.lms.domain.course.spec.rebuild.RebuildSection;
import com.lms.util.Validation;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.lms.domain.course.constvalue.ConstValue.NEXT_SEQ;

@Getter
public class Section {
    private Integer id;
    private String name;
    private Integer seq;
    private List<Content> contents;

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
        List<Content> initialContents = Optional.ofNullable(rebuildSection.contents())
            .orElse(List.of())
            .stream().map(Content::rebuild).toList();

        return new Section(
            rebuildSection.id(), rebuildSection.name(),
            rebuildSection.seq(), new ArrayList<>(initialContents)
        );
    }

    void addContent(Content newContent) {
        balanceSeqBeforeContentAdded(newContent);

        contents.add(newContent);
        contents.sort(Comparator.comparing(Content::getSeq));
    }

    private void balanceSeqBeforeContentAdded(Content newContent) {
        if (isLastSequence(newContent.getSeq())) {
            newContent.specifiedSeq(getLastSeq());
        }

        this.contents.stream()
            .filter(content -> shouldShiftSeq(newContent, content))
            .forEach(Content::nextSeq);
    }

    void deleteContent(Long contentId) {
        if (contents.removeIf(content -> Objects.equals(content.getId(),contentId))){
            balanceSeqAfterContentDeleted();
        }
    }

    void nextSeq() {
        this.seq++;
    }

    void specifiedSeq(Integer specifiedSeq) {
        this.seq =  specifiedSeq;
    }

    void rename(String name) {
        validateName(name);
        this.name = name;
    }

    void renameContent(Long contentId, String name) {
        findContentOrThrow(contentId).rename(name);
    }

    private Content findContentOrThrow(Long contentId) {
        return contents.stream()
            .filter(content -> Objects.equals(content.getId(), contentId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("컨텐츠를 찾을 수 없습니다. id=" + contentId));
    }

    private boolean shouldShiftSeq(Content existing, Content incoming) {
        return existing.getSeq() >= incoming.getSeq();
    }

    private void balanceSeqAfterContentDeleted() {
        AtomicInteger index = new AtomicInteger(NEXT_SEQ);

        contents.stream().sorted(Comparator.comparing(Content::getSeq))
            .forEach(section -> section.specifiedSeq(index.getAndIncrement()));
    }

    private boolean isLastSequence(Integer seq) {
        return contents.size() < seq;
    }

    private Integer getLastSeq() {
        return contents.size() + NEXT_SEQ;
    }

    private void validateName(String name) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("섹션의 이름이 비었습니다.");
        }
    }

    private void validateSeq(Integer seq) throws IllegalArgumentException {
        Optional.ofNullable(seq).orElseThrow(() ->
            new IllegalArgumentException("섹션의 순서 번호가 알맞지 않습니다. 값을 확인해주세요.")
        );

        Validation.requirePositive(seq, "섹션 순서는 음수가 될 수 없습니다.");

    }

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
}
