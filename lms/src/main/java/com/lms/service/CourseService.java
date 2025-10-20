package com.lms.service;

import com.lms.domain.course.*;
import com.lms.domain.course.spec.creation.*;
import com.lms.repository.config.DataSourceFactory;
import com.lms.repository.config.RepositoryConfig;
import com.lms.repository.course.CourseRepository;
import com.lms.repository.course.dto.CourseInfo;
import com.lms.repository.course.dto.CourseInfoSearchFilter;
import com.lms.repository.exception.*;
import com.lms.repository.exception.error.*;
import com.lms.repository.notice.NoticeRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

public class CourseService {

    private final CourseRepository courseRepository = RepositoryConfig.courseRepository();
    private final NoticeRepository noticeRepository = RepositoryConfig.noticeRepository(); // 공지 CRUD용

    // =========================
    // 코스 생성
    // =========================
    public Course createCourse(CreateCourse spec) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            Course course = Course.create(spec);
            courseRepository.create(course);
            conn.commit();
            return course;

        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("코스 생성 중 오류 발생", e);
        } finally {
            clearConnection();
            closeSafely(conn);
        }
    }

    // =========================
    // 코스 조회 (단품)
    // =========================
    public Course findCourseById(Integer courseId) {

        Connection conn;

        try {

            conn = DataSourceFactory.get().getConnection();
            ConnectionHolder.set(conn);

            return courseRepository.findById(courseId)
                    .orElseThrow(() -> new NoSuchElementException("코스를 찾을 수 없습니다."));

        } catch (DatabaseException e) {
            throw new DatabaseError("코스 조회 중 오류가 발생했습니다.", e);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    // =========================
    // 코스 전체 목록 검색
    // =========================
    public List<CourseInfo> searchCourses(CourseInfoSearchFilter filter) {
        if (filter == null)
            throw new IllegalArgumentException("검색 필터가 null일 수 없습니다.");

        Connection conn;

        try {

            conn = DataSourceFactory.get().getConnection();
            ConnectionHolder.set(conn);

            return courseRepository.searchCourseInfo(filter);

        } catch (DatabaseException e) {
            throw new DatabaseError("코스 검색 중 데이터베이스 오류 발생", e);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    // =========================
    // 코스 수정
    // =========================
    public void updateCourse(Course course) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            courseRepository.update(course);
            conn.commit();
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("코스 수정 중 오류 발생", e);
        } finally {
            clearConnection();
            closeSafely(conn);
        }
    }

    // =========================
    // 코스 삭제
    // =========================
    public void deleteCourse(Integer courseId) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            courseRepository.delete(courseId);
            conn.commit();
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("코스 삭제 중 오류 발생", e);
        } finally {
            clearConnection();
            closeSafely(conn);
        }
    }

    // =========================
    // 섹션 / 콘텐츠 관리
    // =========================
    public void addSection(Integer courseId, CreateSection sectionSpec) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new NoSuchElementException("코스를 찾을 수 없습니다."));
            course.addSection(sectionSpec);
            courseRepository.update(course);
            conn.commit();
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("섹션 추가 중 오류 발생", e);
        } finally {
            clearConnection();
            closeSafely(conn);
        }
    }

    public void deleteSection(Integer courseId, Integer sectionId) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new NoSuchElementException("코스를 찾을 수 없습니다."));
            course.deleteSection(sectionId);
            courseRepository.update(course);
            conn.commit();
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("섹션 삭제 중 오류 발생", e);
        } finally {
            clearConnection();
            closeSafely(conn);
        }
    }

    public void addContent(Integer courseId, Integer sectionId, CreateContent contentSpec) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new NoSuchElementException("코스를 찾을 수 없습니다."));
            course.addContent(contentSpec, sectionId);
            courseRepository.update(course);
            conn.commit();
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("콘텐츠 추가 중 오류 발생", e);
        } finally {
            clearConnection();
            closeSafely(conn);
        }
    }

    public void deleteContent(Integer courseId, Integer sectionId, Long contentId) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new NoSuchElementException("코스를 찾을 수 없습니다."));
            course.deleteContent(contentId, sectionId);
            courseRepository.update(course);
            conn.commit();
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("콘텐츠 삭제 중 오류 발생", e);
        } finally {
            clearConnection();
            closeSafely(conn);
        }
    }



    // =========================
    // 내부 유틸
    // =========================
    private void rollbackSafely(Connection conn) {
        if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
    }

    private void clearConnection() {
        try { ConnectionHolder.clear(); } catch (Exception ignored) {}
    }

    private void closeSafely(Connection conn) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
    }
}
