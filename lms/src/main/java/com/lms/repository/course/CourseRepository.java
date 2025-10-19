package com.lms.repository.course;

import com.lms.domain.course.Course;
import com.lms.repository.course.dto.CourseInfo;
import com.lms.repository.course.dto.CourseInfoSearchFilter;
import com.lms.repository.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    /**
     * @param course 강좌 id
     * @return 강좌(null 일 수 있으므로 반드시 확인 해야 함)
     * @throws IllegalArgumentException <br/>
     *      <ul>
     *          <li>course 가 null</li>
     *          <li>course.id 가 not null</li>
     *      </ul>
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    Course create(Course course);

    /**
     * @param id 강좌 id
     * @return 모든 의존성이 채워진 특정한 강좌를 반환
     * @throws IllegalArgumentException id 가 음수
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    Optional<Course> findById(int id);

    /**
     * @param filter 강좌 검색 필터
     * @return 페이징된 강좌 검색 결과.
     * <ul>
     *     <li>반환 결과 내의 각각의 Course 객체의 sections 는 null 입니다.</li>
     *     <li>또한 만약 filter 가 `CourseSearchFilter filter = CourseSearchFilter.builder().build()` 와 같이 구현된 경우.
     *     가장 최근에 수정된 강좌 10 개를 반환합니다.</li>
     * </ul>
     * @throws IllegalArgumentException filter 가 null
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    List<CourseInfo> searchCourseInfo(CourseInfoSearchFilter filter);

    /**
     * 다음과 같이 수정합니다. <br/>
     * 1. 강좌 정보를 수정본으로 덮어씁니다. <br/>
     * 1.1. updated_at 은 기본적으로 수정본의 것으로 수정하나, 수정본의 updated_at 이 null 인 경우 현재 시각으로 설정합니다. <br/>
     * 1.2. created_at 은 설령 수정본에서 수정했다 하더라도 변경하지 않습니다.<br/>
     * 2. 원본에는 있으나 수정본에는 없는 섹션 또는 컨텐츠는 삭제합니다. <br/>
     * 3. 원본에는 없으나 수정본에는 있는 섹션 또는 컨텐츠는 추가합니다. <br/>
     * 4. 원본에도 있고 수정본에도 있는 섹션 또는 컨텐츠는 덮어씁니다.
     * @param course 강좌
     * @throws IllegalArgumentException course 가 null
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    void update(Course course);

    /**
     * @param id 강좌 id
     * @throws IllegalArgumentException id 가 음수
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    void delete(int id);
}
