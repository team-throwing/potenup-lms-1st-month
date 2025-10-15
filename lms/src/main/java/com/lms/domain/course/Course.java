package com.lms.domain.course;

import com.lms.domain.course.spec.creation.CreateCourse;
import com.lms.domain.course.spec.rebuild.RebuildCourse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
public class Course {
    private Integer id;
    private final String title;
    private final String summary;
    private final String detail;
    private final List<Section> sections;
    private final Integer subCategoryId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Course(
        Integer id, String title, String summary,
        String detail, List<Section> sections,
        Integer subCategoryId, LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) throws IllegalArgumentException {
        validateTitle(title);

        this.id = id;
        this.title = title;
        this.summary = summary;
        this.detail = detail;
        this.sections = sections;
        this.subCategoryId = subCategoryId;
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
            rebuildCourse.createdAt(),
            rebuildCourse.updatedAt()
        );
    }

    private void validateTitle(String title) throws IllegalArgumentException {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("강의의 제목이 없습니다. 값을 확인해주세요.");
        }
    }
}
