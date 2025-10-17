package com.lms.repository.course;

import com.lms.domain.course.Course;
import com.lms.domain.course.spec.rebuild.RebuildContent;
import com.lms.domain.course.spec.rebuild.RebuildCourse;
import com.lms.domain.course.spec.rebuild.RebuildSection;
import com.lms.repository.course.dto.CourseInfo;
import com.lms.repository.course.dto.CourseInfoSearchFilter;
import com.lms.repository.exception.DatabaseException;
import com.lms.repository.exception.converter.DbUtils;
import com.lms.service.ConnectionHolder;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class CourseRepositoryImpl implements CourseRepository {

    private final DbUtils dbUtils;
    private final SectionRepository sectionRepository;

    public CourseRepositoryImpl(
            DbUtils dbUtils,
            SectionRepository sectionRepository
    ) {
        this.dbUtils = dbUtils;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Course create(Course course) {

        // 1. 파라미터 검증
        if (course == null) {
            throw new IllegalArgumentException("course 가 null");
        }
        if (course.getId() != null) {
            throw new IllegalArgumentException("course.id 가 null");
        }

        // 2. SQL 작성
        String sql = """
                INSERT INTO course
                (title, category_id, summary, detail, user_id, created_at, updated_at)
                VALUES(?, ?, ?, ?, ?, ?, ?)
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (1): create - executeUpdate -> t/f (with ResultSet for auto inc key)
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                // SQL 와일드 카드에 값 채우기
                pstmt.setString(1, course.getTitle());
                pstmt.setInt(2, course.getSubCategoryId());
                pstmt.setString(3, course.getSummary());
                pstmt.setString(4, course.getDetail());
                pstmt.setLong(5, course.getUserId());
                pstmt.setTimestamp(6, Timestamp.valueOf(course.getCreatedAt()));
                pstmt.setTimestamp(
                        7,
                        course.getUpdatedAt() == null ? null : Timestamp.valueOf(course.getUpdatedAt())
                );

                // SQL 실행
                int result = pstmt.executeUpdate();
                if (result == 0) {
                    throw new DatabaseException("알 수 없는 이유로 생성에 실패했습니다.", null);
                }

                // 처음에는 비어있음
                int id = -1;
                RebuildCourse rebuildCourse = null;
                List<RebuildSection> rebuildSections = new ArrayList<>();
                Map<Integer, List<RebuildContent>> rebuildContentsBySectionId = new HashMap<>();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {

                        // Course.id
                        id = rs.getInt(1);

                        // RebuildCourse
                        rebuildCourse = new RebuildCourse(
                                id,
                                rs.getString("title"),
                                rs.getString("summary"),
                                rs.getString("detail"),
                                rs.getInt("category_id"),
                                rs.getLong("user_id"),

                                // 이 시점에서 rebuildSections 리스트는 비어있음
                                rebuildSections,

                                LocalDateTime.now(),
                                null
                        );
                    }
                }

                // 섹션 리스트 영속화
                rebuildSections
                        = sectionRepository.createAllSectionsOfCourse(course.sections(), id);

                // rebuild 및 반환
                return Course.rebuild(rebuildCourse);
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("CourseRepository.create: rebuild 할 수 없습니다." +
                    "course 내부 상태를 다시 한 번 확인하시기 바랍니다.", e);
        }

        // x. 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
        throw new RuntimeException("CategoryRepositoryImpl.create: 도달 불가능하도록 의도된 지점에 도달했습니다.");
    }

    @Override
    public Optional<Course> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<CourseInfo> searchCourseInfo(CourseInfoSearchFilter filter) {
        return null;
    }

    @Override
    public void update(Course course) {

    }

    @Override
    public void delete(int id) {

    }
}
