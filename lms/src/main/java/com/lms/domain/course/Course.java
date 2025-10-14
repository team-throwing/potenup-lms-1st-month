package com.lms.domain.course;

import com.lms.domain.category.Category;
import com.lms.domain.course.spec.CreateCourse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

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
        List<Section> sections = createCourse.sections().stream().map(Section::create).toList();

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

    public static Course rebuild(
        Integer id, String title, String summary,
        String detail, List<Section> sections, Integer subCategoryId,
        LocalDateTime createdAt,  LocalDateTime updatedAt
    ) throws IllegalArgumentException {
        return new Course(
            id,
            title,
            summary,
            detail,
            sections,
            subCategoryId,
            createdAt,
            updatedAt
        );
    }

    private void validateTitle(String title) throws IllegalArgumentException {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("강의의 제목이 없습니다. 값을 확인해주세요.");
        }
    }
}
