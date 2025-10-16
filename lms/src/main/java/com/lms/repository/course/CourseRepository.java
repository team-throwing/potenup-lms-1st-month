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
     * @throws IllegalArgumentException <br/>
     *      <ul>
     *          <li>course 가 null</li>
     *          <li>course.id 가 not null</li>
     *      </ul>
     * @throws DatabaseException 복구 가능한 데이터베이스 예외입니다. 예를 들어, <br/>
     * <ul>
     *     <li>무결성 제약 조건 위반 (ex. 중복 키 삽입 시도, Not Null 위반, ...)</li>
     *     <li>잘못된 데이터 형식 (ex. 너무 긴 문자열, 오버 플로우, 언더 플로우, 잘못된 날짜 형식, ...)</li>
     *     <li>트랜잭션 실패 (ex. 데드락 감지, 락 대기시간 초과, ...)</li>
     *     <li>등등...</li>
     * </ul>
     */
    void create(Course course);

    /**
     * @param id 강좌 id
     * @return 모든 의존성이 채워진 특정한 강좌를 반환
     * @throws IllegalArgumentException id 가 음수
     * @throws DatabaseException 복구 가능한 데이터베이스 예외입니다. 예를 들어, <br/>
     * <ul>
     *     <li>무결성 제약 조건 위반 (ex. 중복 키 삽입 시도, Not Null 위반, ...)</li>
     *     <li>잘못된 데이터 형식 (ex. 너무 긴 문자열, 오버 플로우, 언더 플로우, 잘못된 날짜 형식, ...)</li>
     *     <li>트랜잭션 실패 (ex. 데드락 감지, 락 대기시간 초과, ...)</li>
     *     <li>등등...</li>
     * </ul>
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
     * @throws DatabaseException 복구 가능한 데이터베이스 예외입니다. 예를 들어, <br/>
     * <ul>
     *     <li>무결성 제약 조건 위반 (ex. 중복 키 삽입 시도, Not Null 위반, ...)</li>
     *     <li>잘못된 데이터 형식 (ex. 너무 긴 문자열, 오버 플로우, 언더 플로우, 잘못된 날짜 형식, ...)</li>
     *     <li>트랜잭션 실패 (ex. 데드락 감지, 락 대기시간 초과, ...)</li>
     *     <li>등등...</li>
     * </ul>
     */
    List<CourseInfo> searchCourseInfo(CourseInfoSearchFilter filter);

    /**
     * @param course 강좌
     * @throws IllegalArgumentException course 가 null
     * @throws DatabaseException 복구 가능한 데이터베이스 예외입니다. 예를 들어, <br/>
     * <ul>
     *     <li>무결성 제약 조건 위반 (ex. 중복 키 삽입 시도, Not Null 위반, ...)</li>
     *     <li>잘못된 데이터 형식 (ex. 너무 긴 문자열, 오버 플로우, 언더 플로우, 잘못된 날짜 형식, ...)</li>
     *     <li>트랜잭션 실패 (ex. 데드락 감지, 락 대기시간 초과, ...)</li>
     *     <li>등등...</li>
     * </ul>
     */
    void update(Course course);

    /**
     * @param id 강좌 id
     * @throws IllegalArgumentException id 가 음수
     * @throws DatabaseException 복구 가능한 데이터베이스 예외입니다. 예를 들어, <br/>
     * <ul>
     *     <li>무결성 제약 조건 위반 (ex. 중복 키 삽입 시도, Not Null 위반, ...)</li>
     *     <li>잘못된 데이터 형식 (ex. 너무 긴 문자열, 오버 플로우, 언더 플로우, 잘못된 날짜 형식, ...)</li>
     *     <li>트랜잭션 실패 (ex. 데드락 감지, 락 대기시간 초과, ...)</li>
     *     <li>등등...</li>
     * </ul>
     */
    void delete(long id);
}
