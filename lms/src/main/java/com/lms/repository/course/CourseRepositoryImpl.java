package com.lms.repository.course;

import com.lms.domain.category.Category;
import com.lms.domain.course.Course;
import com.lms.domain.course.Section;
import com.lms.domain.course.spec.rebuild.RebuildContent;
import com.lms.domain.course.spec.rebuild.RebuildCourse;
import com.lms.domain.course.spec.rebuild.RebuildSection;
import com.lms.repository.course.dto.CourseInfo;
import com.lms.repository.course.dto.CourseInfoSearchFilter;
import com.lms.repository.course.dto.KeywordSearchScope;
import com.lms.repository.exception.DatabaseException;
import com.lms.repository.exception.ModificationTargetNotFoundException;
import com.lms.repository.exception.converter.DbUtils;
import com.lms.service.ConnectionHolder;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        // Course.id
                        id = rs.getInt(1);
                    }
                }

                // 섹션 리스트 영속화
                rebuildSections
                        = sectionRepository.createAllSectionsOfCourse(course.sections(), id);

                // RebuildCourse
                rebuildCourse = new RebuildCourse(
                        id,
                        course.getTitle(),
                        course.getSummary(),
                        course.getDetail(),
                        course.getSubCategoryId(),
                        course.getUserId(),

                        // 이 시점에서 rebuildSections 리스트는 비어있음
                        rebuildSections,

                        course.getCreatedAt(),
                        course.getUpdatedAt()
                );

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

        // 1. 파라미터 검증
        if (id < 0) {
            throw new IllegalArgumentException("id 가 음수입니다.");
        }

        // 2. SQL 작성
        String sql = """
                SELECT
                    course.id as course_id, course.title as course_title, course.category_id as course_category_id,
                    course.summary as course_summary, course.detail as course_detail, course.user_id as course_user_id,
                    course.created_at as course_created_at, course.updated_at as course_updated_at,
                    
                    section.id as section_id, section.name as section_name, section.seq as section_seq,
                    
                    content.id as content_id, content.name as content_name, content.seq as content_seq,
                    content.body as content_body
                FROM course
                LEFT JOIN section ON course.id = section.course_id
                LEFT JOIN content ON section.id = content.section_id
                WHERE course.id=?;
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (3): select- executeQuery -> ResultSet
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // SQL 와일드 카드에 값 채우기
                pstmt.setInt(1, id);

                // SQL 실행
                RebuildCourse rebuildCourse = null;
                List<RebuildSection> rebuildSections = new ArrayList<>();
                Map<Integer, List<RebuildContent>> rebuildContentsBySection = new HashMap<>();
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {

                        if (rebuildCourse == null) {
                            Timestamp updated_at = rs.getTimestamp("course_updated_at");
                            rebuildCourse = new RebuildCourse(
                                    rs.getInt("course_id"),
                                    rs.getString("course_title"),
                                    rs.getString("course_summary"),
                                    rs.getString("course_detail"),
                                    rs.getInt("course_category_id"),
                                    rs.getLong("course_user_id"),
                                    rebuildSections,
                                    rs.getTimestamp("course_created_at").toLocalDateTime(),
                                    updated_at == null ? null : updated_at.toLocalDateTime()
                            );
                        }

                        int sectionId = rs.getInt("section_id");
                        rebuildContentsBySection.compute(sectionId, (k, v) -> {
                            try {

                                if (v == null) {
                                    v = new ArrayList<>();
                                    rebuildSections.add(new RebuildSection(
                                            sectionId,
                                            rs.getInt("section_seq"),
                                            rs.getString("section_name"),
                                            v
                                    ));
                                }

                                v.add(new RebuildContent(
                                        rs.getLong("content_id"),
                                        rs.getString("content_name"),
                                        rs.getInt("content_seq"),
                                        rs.getString("content_body")
                                ));

                                return v;

                            } catch (SQLException e) {
                                dbUtils.handleSQLException(conn, e);

                                // 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
                                return null;
                            }
                        });
                    }
                }

                // Course 생성 및 반환
                if (rebuildCourse == null) {
                    return Optional.empty();
                }
                return Optional.of(Course.rebuild(rebuildCourse));
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        // x. 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
        throw new RuntimeException("CategoryRepositoryImpl.create: 도달 불가능하도록 의도된 지점에 도달했습니다.");
    }

    @Override
    public List<CourseInfo> searchCourseInfo(CourseInfoSearchFilter filter) {

        // 1. 파라미터 검증
        if (filter == null) {
            throw new IllegalArgumentException("filter 가 null 입니다.");
        }

        // 2. SQL 작성
        // sql 앞 부분
        String foreSql = """
                WITH search_course_cte AS (
                	SELECT c.id, title, summary, detail, category_id, created_at, updated_at, user_id
                 		, u.name as user_name
                 		, ROW_NUMBER() OVER(ORDER BY id) AS row_num
                 	FROM course c JOIN user u ON c.user_id = u.id
                    WHERE true
                """;

        // 검색 필터 적용
        StringBuilder sql = new StringBuilder(foreSql).append("\n\t");

        // 키워드
        String keyword = filter.keyword();
        boolean isKeywordNotNullAndNotBlank = (keyword != null && !keyword.isBlank());
        boolean isUserNamePresented = filter.userName() != null;
        if (isKeywordNotNullAndNotBlank) {
            if (filter.keywordSearchScope() == KeywordSearchScope.TITLE) {
                sql.append("AND MATCH(title) AGAINST(?)").append("\n\t");
            } else if (filter.keywordSearchScope() == KeywordSearchScope.TITLE_SUMMARY) {
                sql.append("AND MATCH(title, summary) AGAINST(?)").append("\n\t");
            } else {
                sql.append("AND MATCH(title, summary, detail) AGAINST(?)").append("\n\t");
            }

            // keyword 가 존재하면서 userName 이 제시되지 않은 경우 keyword 로 userName 도 검색
            if (!isUserNamePresented) {
                sql.append("OR u.name LIKE ?").append("\n\t");  // ? 위치에 %% 로 감싸진 키워드 삽입
            }
        }

        // 카테고리 범위
        List<Category> categorySearchScope = filter.categorySearchScope();
        boolean isCategorySearchScopePresented
                = (categorySearchScope != null && !categorySearchScope.isEmpty());
        if (isCategorySearchScopePresented) {
            sql.append("AND category_id IN (");

            Set<Integer> categoryIds = new HashSet<>();
            for (Category category : categorySearchScope) {
                categoryIds.add(category.getId());
            }

            sql.append(categoryIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","))
            ).append(")").append("\n\t");
        }

        // createdAt 범위
        Timestamp createdAtFromTimestamp
                = filter.createdAtFrom() == null ? null : Timestamp.valueOf(filter.createdAtFrom());
        Timestamp createdAtToTimestamp
                = filter.createdAtTo() == null ? null : Timestamp.valueOf(filter.createdAtTo());
        boolean isCreatedAtFromPresented = createdAtFromTimestamp != null;
        boolean isCreatedAtToPresented = createdAtToTimestamp != null;
        if (isCreatedAtFromPresented) {
            sql.append("AND ? <= created_at").append("\n\t");
        }
        if (isCreatedAtToPresented) {
            sql.append("AND created_at <= ?").append("\n\t");
        }

        // updatedAt 범위
        Timestamp updatedAtFromTimestamp
                = filter.updatedAtFrom() == null ? null : Timestamp.valueOf(filter.updatedAtFrom());
        Timestamp updatedAtToTimestamp
                = filter.updatedAtTo() == null ? null : Timestamp.valueOf(filter.updatedAtTo());
        boolean isUpdatedAtFromPresented = updatedAtFromTimestamp != null;
        boolean isUpdatedAtToPresented = updatedAtToTimestamp != null;
        if (isUpdatedAtFromPresented) {
            sql.append("AND ? <= updated_at").append("\n\t");
        }
        if (isUpdatedAtToPresented) {
            sql.append("AND updated_at <= ?").append("\n\t");
        }

        // user_name
        if (isUserNamePresented) {
            sql.append("AND u.name LIKE ?").append("\n\t");  // ? 위치에 %% 로 감싸진 userName 삽입
        }

        // sql 뒷 부분
        String rearSql = """
                )
                SELECT id, title, summary, detail, category_id, created_at, updated_at, user_id, user_name
                	, (SELECT count(*) FROM search_course_cte) AS total_count
                FROM search_course_cte
                WHERE row_num BETWEEN ((? - 1) * ? + 1) AND ((? - 1) * ? + ?)
                """;
        sql.append(rearSql);

//        테스트 용
//        System.out.println("************");
//        System.out.println(sql.toString());

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (3): select- executeQuery -> ResultSet
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

                // SQL 와일드 카드에 값 채우기
                int wildcardSeq = 0;
                // 키워드
                if (isKeywordNotNullAndNotBlank) {
                    pstmt.setString(++wildcardSeq, keyword);
                    if (!isUserNamePresented) {
                        pstmt.setString(++wildcardSeq, "%" + keyword + "%");
                    }
                }
                // createdAt
                if (isCreatedAtFromPresented) {
                    pstmt.setTimestamp(++wildcardSeq, createdAtFromTimestamp);
                }
                if (isCreatedAtToPresented) {
                    pstmt.setTimestamp(++wildcardSeq, createdAtToTimestamp);
                }
                // updatedAt
                if (isUpdatedAtFromPresented) {
                    pstmt.setTimestamp(++wildcardSeq, updatedAtFromTimestamp);
                }
                if (isUpdatedAtToPresented) {
                    pstmt.setTimestamp(++wildcardSeq, updatedAtToTimestamp);
                }
                // userName
                if (isUserNamePresented) {
                    pstmt.setString(++wildcardSeq, filter.userName());
                }
                // WHERE row_num BETWEEN ((? - 1) * ? + 1) AND ((? - 1) * ? + ?)
                pstmt.setInt(++wildcardSeq, filter.pageNum());
                pstmt.setInt(++wildcardSeq, filter.pageSize());
                pstmt.setInt(++wildcardSeq, filter.pageNum());
                pstmt.setInt(++wildcardSeq, filter.pageSize());
                pstmt.setInt(++wildcardSeq, filter.pageSize());

                // SQL 실행
                List<CourseInfo> courseInfoList = new ArrayList<>();
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
                        Timestamp updatedAtTimestamp = rs.getTimestamp("updated_at");
                        courseInfoList.add(new CourseInfo(
                                rs.getInt("id"),
                                rs.getString("title"),
                                rs.getString("summary"),
                                rs.getString("detail"),
                                rs.getInt("category_id"),
                                createdAtTimestamp == null ? null : createdAtTimestamp.toLocalDateTime(),
                                updatedAtTimestamp == null ? null : updatedAtTimestamp.toLocalDateTime(),
                                rs.getLong("user_id"),
                                rs.getString("user_name"),
                                rs.getLong("total_count")
                        ));
                    }
                }

                // 결과를 적절하게 처리
                return courseInfoList;
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        // x. 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
        throw new RuntimeException("CategoryRepositoryImpl.create: 도달 불가능하도록 의도된 지점에 도달했습니다.");
    }

    @Override
    public void update(Course updatedCourse) {

        // 1. 파라미터 검증
        if (updatedCourse == null) {
            throw new IllegalArgumentException("updatedCourse 가 null");
        }

        // 2. 업데이트 하지 않은 원래 강좌를 가져오기
        Course originalCourse = null;
        Optional<Course> optionalCourse = findById(updatedCourse.getId());
        if (optionalCourse.isEmpty()) {
            throw new ModificationTargetNotFoundException("수정할 강좌를 찾지 못했습니다.", null);
        }
        originalCourse = optionalCourse.get();

        /*
            1. updatedCourse 를 업데이트 한다.
            2. 삭제 부분(원본 - 수정본)은 delete 를 수행한다.
                2.1. section 먼저
                2.2. content 그 다음
            3. 교집합과 추가 부분(수정본-원본) == 수정본은 insert into on duplicate key update 를 수행한다.
                3.1. section
                3.2. content
         */

        // 2. SQL 작성
        String sql = """
                UPDATE course
                SET
                    title=?,
                    category_id=?,
                    summary=?,
                    detail=?,
                    user_id=?,
                    updated_at=?
                WHERE id=?
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (2): update | delete - executeUpdate -> affectedRowCount (no ResultSet)
            try (PreparedStatement pstmt = conn.prepareStatement(sql /*, Statement.RETURN_GENERATED_KEYS*/)) {

                // SQL 와일드 카드에 값 채우기
                pstmt.setString(1, updatedCourse.getTitle());
                pstmt.setInt(2, updatedCourse.getSubCategoryId());
                pstmt.setString(3, updatedCourse.getSummary());
                pstmt.setString(4, updatedCourse.getDetail());
                pstmt.setLong(5, updatedCourse.getUserId());
                if (updatedCourse.getUpdatedAt() == null) {
                    pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                } else {
                    pstmt.setTimestamp(6, Timestamp.valueOf(updatedCourse.getUpdatedAt()));
                }
                pstmt.setInt(7, updatedCourse.getId());

                // SQL 실행
                boolean isNotUpdated = pstmt.executeUpdate() <= 0;
                if (isNotUpdated) {
                    throw new ModificationTargetNotFoundException("수정할 강좌를 찾을 수 없습니다.", null);
                }

                // 원본 섹션 id 집합 만들기
                Set<Integer> originalSectionIdSet = new HashSet<>();
                List<Section> originalSections = originalCourse.sections();
                for (Section section : originalSections) {
                    originalSectionIdSet.add(section.getId());
                }

                // 수정본 섹션 id 집합 만들기
                Set<Integer> toBeUpdatedSectionIdSet = new HashSet<>();
                List<Section> updatedSections = updatedCourse.sections();
                for (Section section : updatedSections) {
                    if (section.getId() != null) {
                        toBeUpdatedSectionIdSet.add(section.getId());
                    }
                }

                // 제거 대상인 차집합(원본 - 수정본) 섹션 id 집합을 만듭니다.
                Set<Integer> toBeDeletedSectionIdSet = new HashSet<>(originalSectionIdSet);
                toBeDeletedSectionIdSet.removeAll(toBeUpdatedSectionIdSet);
                
                // 섹션 삭제
                if (!toBeDeletedSectionIdSet.isEmpty()) {
                    sectionRepository.deleteAll(toBeDeletedSectionIdSet);
                }
                
                // 섹션 수정 - 이 과정에서 컨텐츠 역시 삭제 또는 수정됩니다.
                sectionRepository.updateAllGivenSectionsOfCourse(updatedSections, originalCourse);
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }
    }

    @Override
    public void delete(int id) {

        // 1. 파라미터 검증
        if (id < 0) {
            throw new IllegalArgumentException("id 가 음수");
        }

        // 2. SQL 작성
        String sql = """
                DELETE FROM course
                WHERE id=?
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (1): create - executeUpdate -> t/f (with ResultSet for auto inc key)
            // a. 처리 유형 (2): update | delete - executeUpdate -> affectedRowCount (no ResultSet)
            // a. 처리 유형 (3): select- executeQuery -> ResultSet
            try (PreparedStatement pstmt = conn.prepareStatement(sql /*, Statement.RETURN_GENERATED_KEYS*/)) {

                // SQL 와일드 카드에 값 채우기
                pstmt.setInt(1, id);

                // SQL 실행
                boolean isDeleteFail = pstmt.executeUpdate() <= 0;
                if (isDeleteFail) {
                    throw new ModificationTargetNotFoundException("삭제할 강좌를 찾을 수 없습니다.", null);
                }

                // 결과를 적절하게 처리
                return;
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        // x. 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
        throw new RuntimeException("CategoryRepositoryImpl.create: 도달 불가능하도록 의도된 지점에 도달했습니다.");
    }
}
