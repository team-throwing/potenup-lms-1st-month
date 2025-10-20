package com.lms.repository.course;

import com.lms.domain.course.Course;
import com.lms.domain.course.Section;
import com.lms.domain.course.spec.rebuild.RebuildSection;
import com.lms.repository.exception.DatabaseException;

import java.util.List;
import java.util.Set;

public interface SectionRepository {

    /**
     * @param sections id 세팅되지 않은 섹션들의 리스트
     * @param courseId 부모 강좌
     * @return 강좌의 섹션 목록
     * @throws IllegalArgumentException sections 가 null 또는 empty
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    List<RebuildSection> createAllSectionsOfCourse(List<Section> sections, int courseId);

    /**
     * @param sectionIds 삭제할 섹션 id 집합
     * @throws IllegalArgumentException sectionIds 가 null 또는 empty
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    void deleteAll(Set<Integer> sectionIds);

    /**
     * 1. sections 중 id 가 null 인 것들은 생성을 시도합니다. </br>
     * 2. sections 중 id 가 null 이 아닌 것들은 덮어쓰기를 시도합니다.<br/>
     * * (데이터베이스에 해당 id 의 섹션이 이미 있는 경우 수정, 없는 경우 생성)
     * @param sections 수정 또는 삽입할 섹션 목록
     * @param originalCourse 소속 강좌
     * @throws IllegalArgumentException null 또는 empty
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    void updateAllGivenSectionsOfCourse(List<Section> sections, Course originalCourse);
}
