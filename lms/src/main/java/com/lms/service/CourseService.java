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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public class CourseService {

    private final CourseRepository courseRepository = RepositoryConfig.courseRepository();

    // =========================
    // 코스 생성
    // =========================
    public Course createCourse(CreateCourse spec) {
        try (Connection conn = DataSourceFactory.get().getConnection()) {
            conn.setAutoCommit(false);

            Course course = Course.create(spec);
            courseRepository.create(course);

            conn.commit();
            return course;

        } catch (DuplicateKeyException e) {
            throw new DatabaseException("이미 존재하는 코스입니다.", e);
        } catch (ConstraintViolationException e) {
            throw new DatabaseException("코스 생성 시 무결성 제약조건 위반이 발생했습니다.", e);
        } catch (TransactionException e) {
            throw new DatabaseError("코스 생성 중 트랜잭션 오류가 발생했습니다.", e);
        } catch (DatabaseAccessError e) {
            throw new DatabaseError("데이터베이스 연결 오류로 코스 생성에 실패했습니다.", e);
        } catch (DatabaseException e) {
            throw new DatabaseException("코스 생성 중 데이터베이스 예외가 발생했습니다.", e);
        } catch (SQLException e) {
            throw new DatabaseError("코스 생성 중 SQLException 발생", e);
        }
    }

    // =========================
    // 코스 조회 (단품)
    // =========================
    public Course findCourseById(Integer courseId) {
        try {
            return courseRepository.findById(courseId)
                    .orElseThrow(() -> new NoSuchElementException("코스를 찾을 수 없습니다."));
        } catch (DatabaseException e) {
            throw new DatabaseException("코스 조회 중 오류가 발생했습니다.", e);
        }
    }

    // =========================
    // 코스 전체 목록 검색
    // =========================
    public List<CourseInfo> searchCourses(CourseInfoSearchFilter filter) {
        if (filter == null)
            throw new IllegalArgumentException("검색 필터가 null일 수 없습니다.");

        try {
            return courseRepository.searchCourseInfo(filter);
        } catch (ConstraintViolationException e) {
            throw new DatabaseException("검색 조건이 잘못되었습니다.", e);
        } catch (DatabaseAccessError e) {
            throw new DatabaseError("데이터베이스 접근 중 오류 발생", e);
        } catch (DatabaseException e) {
            throw new DatabaseException("코스 검색 중 데이터베이스 오류 발생", e);
        }
    }

    // =========================
    // 코스 수정
    // =========================
    public void updateCourse(Course course) {
        try (Connection conn = DataSourceFactory.get().getConnection()) {
            conn.setAutoCommit(false);

            courseRepository.update(course);
            conn.commit();

        } catch (ConstraintViolationException e) {
            throw new DatabaseException("코스 수정 시 무결성 제약조건 위반이 발생했습니다.", e);
        } catch (TransactionException e) {
            throw new DatabaseError("코스 수정 중 트랜잭션 오류가 발생했습니다.", e);
        } catch (DatabaseException e) {
            throw new DatabaseException("코스 수정 중 데이터베이스 오류가 발생했습니다.", e);
        } catch (SQLException e) {
            throw new DatabaseError("코스 수정 중 SQLException 발생", e);
        }
    }

    // =========================
    // 코스 삭제
    // =========================
    public void deleteCourse(Integer courseId) {
        try (Connection conn = DataSourceFactory.get().getConnection()) {
            conn.setAutoCommit(false);

            courseRepository.delete(courseId);
            conn.commit();

        } catch (ForeignKeyConstraintException e) {
            throw new DatabaseException("해당 코스를 참조 중인 데이터가 존재합니다.", e);
        } catch (TransactionException e) {
            throw new DatabaseError("코스 삭제 중 트랜잭션 오류가 발생했습니다.", e);
        } catch (DatabaseAccessError e) {
            throw new DatabaseError("데이터베이스 접근 오류가 발생했습니다.", e);
        } catch (DatabaseException e) {
            throw new DatabaseException("코스 삭제 중 데이터베이스 오류가 발생했습니다.", e);
        } catch (SQLException e) {
            throw new DatabaseError("코스 삭제 중 SQLException 발생", e);
        }
    }

    // =========================
    // 섹션/콘텐츠 관리
    // =========================
    public void addSection(Integer courseId, CreateSection sectionSpec) {
        try (Connection conn = DataSourceFactory.get().getConnection()) {
            conn.setAutoCommit(false);

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new NoSuchElementException("코스를 찾을 수 없습니다."));

            course.addSection(sectionSpec);
            courseRepository.update(course);

            conn.commit();
        } catch (ConstraintViolationException e) {
            throw new DatabaseException("섹션 추가 중 무결성 제약조건 위반", e);
        } catch (DatabaseException e) {
            throw new DatabaseException("섹션 추가 중 데이터베이스 오류 발생", e);
        } catch (SQLException e) {
            throw new DatabaseError("섹션 추가 중 SQLException 발생", e);
        }
    }

    public void deleteSection(Integer courseId, Integer sectionId) {
        try (Connection conn = DataSourceFactory.get().getConnection()) {
            conn.setAutoCommit(false);

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new NoSuchElementException("코스를 찾을 수 없습니다."));

            course.deleteSection(sectionId);
            courseRepository.update(course);

            conn.commit();
        } catch (ConstraintViolationException e) {
            throw new DatabaseException("섹션 삭제 중 무결성 제약조건 위반", e);
        } catch (DatabaseException e) {
            throw new DatabaseException("섹션 삭제 중 데이터베이스 오류 발생", e);
        } catch (SQLException e) {
            throw new DatabaseError("섹션 삭제 중 SQLException 발생", e);
        }
    }

    public void addContent(Integer courseId, Integer sectionId, CreateContent contentSpec) {
        try (Connection conn = DataSourceFactory.get().getConnection()) {
            conn.setAutoCommit(false);

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new NoSuchElementException("코스를 찾을 수 없습니다."));

            course.addContent(contentSpec, sectionId);
            courseRepository.update(course);

            conn.commit();
        } catch (ConstraintViolationException e) {
            throw new DatabaseException("콘텐츠 추가 중 무결성 제약조건 위반", e);
        } catch (DatabaseException e) {
            throw new DatabaseException("콘텐츠 추가 중 데이터베이스 오류 발생", e);
        } catch (SQLException e) {
            throw new DatabaseError("콘텐츠 추가 중 SQLException 발생", e);
        }
    }

    public void deleteContent(Integer courseId, Integer sectionId, Long contentId) {
        try (Connection conn = DataSourceFactory.get().getConnection()) {
            conn.setAutoCommit(false);

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new NoSuchElementException("코스를 찾을 수 없습니다."));

            course.deleteContent(contentId, sectionId);
            courseRepository.update(course);

            conn.commit();
        } catch (ConstraintViolationException e) {
            throw new DatabaseException("콘텐츠 삭제 중 무결성 제약조건 위반", e);
        } catch (DatabaseException e) {
            throw new DatabaseException("콘텐츠 삭제 중 데이터베이스 오류 발생", e);
        } catch (SQLException e) {
            throw new DatabaseError("콘텐츠 삭제 중 SQLException 발생", e);
        }
    }

    // =========================
    // 공지사항 CRUD
    // =========================

    public Notice createNotice(String body) {
        try (Connection conn = DataSourceFactory.get().getConnection()) {
            conn.setAutoCommit(false);

            Notice notice = new Notice(null, body).create(body);
            noticeRepository.create(notice);

            conn.commit();
            return notice;

        } catch (DatabaseException e) {
            throw new DatabaseError("공지사항 생성 중 데이터베이스 오류 발생", e);
        } catch (SQLException e) {
            throw new DatabaseError("공지사항 생성 중 SQLException 발생", e);
        }
    }

    public Notice findNoticeById(Long id) {
        try {
            return noticeRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("공지사항을 찾을 수 없습니다."));
        } catch (DatabaseException e) {
            throw new DatabaseError("공지사항 조회 중 데이터베이스 오류 발생", e);
        }
    }

    public void updateNotice(Long id, String newBody) {
        try (Connection conn = DataSourceFactory.get().getConnection()) {
            conn.setAutoCommit(false);

            Notice notice = noticeRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("공지사항을 찾을 수 없습니다."));

            notice = notice.rebuild(id, newBody);
            noticeRepository.update(notice);

            conn.commit();

        } catch (DatabaseException e) {
            throw new DatabaseError("공지사항 수정 중 데이터베이스 오류 발생", e);
        } catch (SQLException e) {
            throw new DatabaseError("공지사항 수정 중 SQLException 발생", e);
        }
    }

    public void deleteNotice(Long id) {
        try (Connection conn = DataSourceFactory.get().getConnection()) {
            conn.setAutoCommit(false);

            noticeRepository.delete(id);
            conn.commit();

        } catch (DatabaseException e) {
            throw new DatabaseError("공지사항 삭제 중 데이터베이스 오류 발생", e);
        } catch (SQLException e) {
            throw new DatabaseError("공지사항 삭제 중 SQLException 발생", e);
        }
    }
}
