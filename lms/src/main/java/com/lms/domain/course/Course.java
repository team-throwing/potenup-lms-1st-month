package com.lms.domain.course;

import com.lms.domain.course.spec.creation.CreateContent;
import com.lms.domain.course.spec.creation.CreateCourse;
import com.lms.domain.course.spec.creation.CreateSection;
import com.lms.domain.course.spec.rebuild.RebuildCourse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.lms.domain.course.constvalue.ConstValue.NEXT_SEQ;

public class Course {
    @Getter
    private Integer id;
    @Getter
    private final String title;
    @Getter
    private final String summary;
    @Getter
    private final String detail;
    private final List<Section> sections;
    @Getter
    private final Integer subCategoryId;
    @Getter
    private final LocalDateTime createdAt;
    @Getter
    private final Long userId;
    @Getter
    private LocalDateTime updatedAt;

    private Course(
        Integer id, String title, String summary,
        String detail, List<Section> sections,
        Integer subCategoryId, Long userId,
        LocalDateTime createdAt, LocalDateTime updatedAt
    ) throws IllegalArgumentException {
        validateTitle(title);

        this.id = id;
        this.title = title;
        this.summary = summary;
        this.detail = detail;
        this.sections = sections;
        this.subCategoryId = subCategoryId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Course create(CreateCourse createCourse) throws IllegalArgumentException {
        List<Section> sections = Optional.of(createCourse.sections())
            .orElse(List.of())
            .stream().map(Section::create).toList();

        return new Course(
            null,
            createCourse.title(),
            createCourse.summary(),
            createCourse.detail(),
            sections,
            createCourse.subCategoryId(),
            createCourse.userId(),
            LocalDateTime.now(),
            null
        );
    }

    public static Course rebuild(RebuildCourse rebuildCourse) throws IllegalArgumentException {
        List<Section> initialSections = Optional.ofNullable(rebuildCourse.sections())
            .orElse(List.of())
            .stream().map(Section::rebuild).toList();

        return new Course(
            rebuildCourse.id(),
            rebuildCourse.title(),
            rebuildCourse.summary(),
            rebuildCourse.detail(),
            initialSections,
            rebuildCourse.subCategoryId(),
            rebuildCourse.userId(),
            rebuildCourse.createdAt(),
            rebuildCourse.updatedAt()
        );
    }

    public List<Section> sections() {
        return List.copyOf(sections);
    }

    public void addSection(CreateSection section) {
        if (section == null) return;

        Section newSection = Section.create(section);

        /*
         * 만약 같은 seq가 존재할 경우
         * 기존의 section의 순번을 1만큼 뒤로 밀고
         * 기존의 순번 자리에 새로운 섹션을 넣는다.
         */
        balanceSeq(newSection);
        sections.add(Section.create(section));

        touched();
    }

    private void balanceSeq(Section newSection) {
        if (isLastSequence(newSection.getSeq())) {
            newSection.specifiedSeq(getLastSeq());
        }

        this.sections.stream()
            .filter(section -> section.getSeq().equals(newSection.getSeq()))
            .findFirst().ifPresent(Section::nextSeq);
    }

    public void addContent(CreateContent content, Integer sectionId) {
        if (content == null) return;

        Content newContent = Content.create(content);

        sections.stream().filter(section -> section.getId().equals(sectionId))
            .findFirst()
            .ifPresent(section -> section.addContent(newContent));

        touched();
    }

    private boolean isLastSequence(Integer seq) {
        return sections.size() < seq;
    }

    private Integer getLastSeq() {
        return sections.size() + NEXT_SEQ;
    }

    private void validateTitle(String title) throws IllegalArgumentException {
        Optional.ofNullable(title).orElseThrow(() ->
            new IllegalArgumentException("강의의 제목이 없습니다. 값을 확인해주세요.")
        );
    }

    private void touched() {
        this.updatedAt = LocalDateTime.now();
    }
}
