package com.lms.repository.course;

import com.lms.domain.course.Section;
import com.lms.domain.course.spec.rebuild.RebuildSection;
import com.lms.repository.exception.DatabaseException;

import java.util.List;

public interface SectionRepository {

    /**
     * @param sections id 세팅되지 않은 섹션들의 리스트
     * @param courseId 부모 강좌
     * @return 강좌의 섹션 목록
     * @throws IllegalArgumentException sections 가 null 또는 empty
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    List<RebuildSection> createAllSectionsOfCourse(List<Section> sections, int courseId);
}
