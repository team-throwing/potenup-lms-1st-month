package com.lms.domain.course;

import com.lms.domain.course.spec.creation.CreateContent;
import com.lms.domain.course.spec.creation.CreateCourse;
import com.lms.domain.course.spec.creation.CreateSection;
import com.lms.domain.course.spec.rebuild.RebuildCourse;
import com.lms.util.Validation;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static Course create(CreateCourse createCourse) throws IllegalArgumentException {
        List<Section> sections = Optional.ofNullable(createCourse.sections())
            .orElse(List.of())
            .stream().map(Section::create).toList();

        return new Course(
            null,
            createCourse.title(),
            createCourse.summary(),
            createCourse.detail(),
            new ArrayList<>(sections),
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
            new ArrayList<>(initialSections),
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
        balanceSeqBeforeSectionAdded(newSection);

        sections.add(newSection);
        sections.sort(Comparator.comparing(Section::getSeq));

        touched();
    }

    public void deleteSection(Integer deletedSectionId) {
        if (sections.removeIf(section -> Objects.equals(section.getId(), deletedSectionId))) {
            balanceSeqAfterSectionDeleted();
            touched();
        }
    }

    public void addContent(CreateContent content, Integer sectionId) {
        if (content == null) return;
        Content newContent = Content.create(content);
        findSectionOrThrow(sectionId).addContent(newContent);
        touched();
    }

    public void deleteContent(Long contentId, Integer sectionId) {
        findSectionOrThrow(sectionId).deleteContent(contentId);
        touched();
    }

    public void renameSection(Integer sectionId, String name) {
        findSectionOrThrow(sectionId).rename(name);
        touched();
    }

    public void renameContent(Long contentId, Integer sectionId, String name) {
        findSectionOrThrow(sectionId).renameContent(contentId, name);
        touched();
    }

    private boolean isLastSequence(Integer seq) {
        return sections.size() < seq;
    }

    private Integer getLastSeq() {
        return sections.size() + NEXT_SEQ;
    }

    private void validateTitle(String title) throws IllegalArgumentException {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("강의의 제목이 없습니다. 값을 확인해주세요.");
        }
    }

    private void validateSubCategoryId(Integer subCategoryId) {
        Optional.ofNullable(subCategoryId).orElseThrow(
            () -> new IllegalArgumentException("하위 카테고리를 선택해주세요.")
        );

        Validation.requirePositive(subCategoryId, "하위 카테고리 아이디는 음수가 될 수 없습니다.");
    }

    private void touched() {
        this.updatedAt = LocalDateTime.now();
    }

    private Section findSectionOrThrow(Integer sectionId) {
        return sections.stream()
            .filter(section -> Objects.equals(section.getId(), sectionId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("섹션을 찾을 수 없습니다. id=" + sectionId));
    }

    private void balanceSeqBeforeSectionAdded(Section newSection) {
        if (isLastSequence(newSection.getSeq())) {
            newSection.specifiedSeq(getLastSeq());
        }

        this.sections.stream()
            .filter(section -> shouldShiftSeq(section, newSection))
            .forEach(Section::nextSeq);
    }

    private boolean shouldShiftSeq(Section existing, Section incoming) {
        return existing.getSeq() >= incoming.getSeq();
    }

    private void balanceSeqAfterSectionDeleted() {
        AtomicInteger index = new AtomicInteger(NEXT_SEQ);

        sections.stream().sorted(Comparator.comparing(Section::getSeq))
            .forEach(section -> section.specifiedSeq(index.getAndIncrement()));
    }

    private Course(
        Integer id, String title, String summary,
        String detail, List<Section> sections,
        Integer subCategoryId, Long userId,
        LocalDateTime createdAt, LocalDateTime updatedAt
    ) throws IllegalArgumentException {
        validateTitle(title);
        validateSubCategoryId(subCategoryId);

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
}
