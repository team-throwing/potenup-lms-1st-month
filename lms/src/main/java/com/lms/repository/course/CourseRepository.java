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
     * @return 강좌
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
    Optional<Course> findById(long id);

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
    void delete(long id);
}
