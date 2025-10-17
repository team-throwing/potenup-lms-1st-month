// ============================================
// Course Service
// ============================================

package com.lms.service;

import com.lms.domain.course.Course;
import com.lms.domain.course.spec.creation.CreateCourse;
import com.lms.repository.category.CategoryRepository;
import com.lms.repository.course.CourseRepository;
import com.lms.repository.course.dto.CourseInfo;
import com.lms.repository.course.dto.CourseInfoSearchFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final DataSource dataSource;

    /**
     * 새로운 강좌를 생성합니다.
     *
     * @param createCourse 강좌 생성 스펙
     * @return 생성된 강좌
     * @throws IllegalArgumentException 요청 데이터가 유효하지 않은 경우
     */
    public Course createCourse(CreateCourse createCourse) {
        validateCreateCourse(createCourse);

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            // 카테고리 존재 여부 검증
            validateCategoryExists(createCourse.subCategoryId());

            Course course = Course.create(createCourse);
            courseRepository.create(course);

            conn.commit();
            log.info("강좌 생성 완료: {}", course.getTitle());

            return course;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    log.error("롤백 실패", rollbackEx);
                }
            }
            log.error("강좌 생성 실패", e);
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException("강좌 생성 중 오류가 발생했습니다.", e);
        } finally {
            ConnectionHolder.clear();
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    log.error("Connection 닫기 실패", closeEx);
                }
            }
        }
    }

    /**
     * 특정 강좌를 조회합니다 (모든 섹션 및 컨텐츠 포함).
     *
     * @param id 강좌 ID
     * @return 강좌 상세 정보
     * @throws RuntimeException 강좌를 찾을 수 없는 경우
     */
    public Course getCourse(long id) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            Course course = courseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("강좌를 찾을 수 없습니다. ID: " + id));

            conn.commit();
            return course;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    log.error("롤백 실패", rollbackEx);
                }
            }
            log.error("강좌 조회 실패", e);
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException("강좌 조회 중 오류가 발생했습니다.", e);
        } finally {
            ConnectionHolder.clear();
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    log.error("Connection 닫기 실패", closeEx);
                }
            }
        }
    }

    /**
     * 강좌 목록을 검색합니다 (필터 적용).
     *
     * @param filter 검색 필터
     * @return 검색된 강좌 정보 목록
     */
    public List<CourseInfo> searchCourses(CourseInfoSearchFilter filter) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            if (filter == null) {
                filter = CourseInfoSearchFilter.builder().build();
            }

            List<CourseInfo> result = courseRepository.searchCourseInfo(filter);

            conn.commit();
            return result;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    log.error("롤백 실패", rollbackEx);
                }
            }
            log.error("강좌 검색 실패", e);
            throw new RuntimeException("강좌 검색 중 오류가 발생했습니다.", e);
        } finally {
            ConnectionHolder.clear();
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    log.error("Connection 닫기 실패", closeEx);
                }
            }
        }
    }

    /**
     * 강좌 정보를 수정합니다.
     *
     * @param course 수정할 강좌 (rebuild된 Course 객체)
     * @throws RuntimeException 강좌를 찾을 수 없거나 수정 실패 시
     */
    public void updateCourse(Course course) {
        if (course == null || course.getId() == null) {
            throw new IllegalArgumentException("수정할 강좌 정보가 유효하지 않습니다.");
        }

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            // 기존 강좌 존재 여부 확인
            courseRepository.findById(course.getId())
                    .orElseThrow(() -> new RuntimeException("강좌를 찾을 수 없습니다. ID: " + course.getId()));

            // 카테고리 존재 여부 검증
            validateCategoryExists(course.getSubCategoryId());

            courseRepository.update(course);

            conn.commit();
            log.info("강좌 수정 완료: ID={}", course.getId());

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    log.error("롤백 실패", rollbackEx);
                }
            }
            log.error("강좌 수정 실패", e);
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException("강좌 수정 중 오류가 발생했습니다.", e);
        } finally {
            ConnectionHolder.clear();
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    log.error("Connection 닫기 실패", closeEx);
                }
            }
        }
    }

    /**
     * 강좌를 삭제합니다.
     *
     * @param id 강좌 ID
     * @throws RuntimeException 강좌를 찾을 수 없는 경우
     */
    public void deleteCourse(long id) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            if (courseRepository.findById(id).isEmpty()) {
                throw new RuntimeException("강좌를 찾을 수 없습니다. ID: " + id);
            }

            courseRepository.delete(id);

            conn.commit();
            log.info("강좌 삭제 완료: ID={}", id);

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    log.error("롤백 실패", rollbackEx);
                }
            }
            log.error("강좌 삭제 실패", e);
            throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException("강좌 삭제 중 오류가 발생했습니다.", e);
        } finally {
            ConnectionHolder.clear();
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    log.error("Connection 닫기 실패", closeEx);
                }
            }
        }
    }

    private void validateCreateCourse(CreateCourse createCourse) {
        if (createCourse == null) {
            throw new IllegalArgumentException("강좌 생성 정보가 없습니다.");
        }
        if (createCourse.title() == null || createCourse.title().isBlank()) {
            throw new IllegalArgumentException("강좌 제목은 필수입니다.");
        }
        if (createCourse.subCategoryId() == null) {
            throw new IllegalArgumentException("카테고리는 필수입니다.");
        }
        if (createCourse.userId() == null) {
            throw new IllegalArgumentException("사용자 정보는 필수입니다.");
        }
    }

    private void validateCategoryExists(Integer categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다. ID: " + categoryId));
    }
}