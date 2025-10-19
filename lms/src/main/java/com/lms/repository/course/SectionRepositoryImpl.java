package com.lms.repository.course;

import com.lms.domain.course.Content;
import com.lms.domain.course.Course;
import com.lms.domain.course.Section;
import com.lms.domain.course.spec.rebuild.RebuildContent;
import com.lms.domain.course.spec.rebuild.RebuildSection;
import com.lms.repository.exception.DatabaseException;
import com.lms.repository.exception.ModificationTargetNotFoundException;
import com.lms.repository.exception.converter.DbUtils;
import com.lms.service.ConnectionHolder;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SectionRepositoryImpl implements SectionRepository {

    private final DbUtils dbUtils;
    private final ContentRepository contentRepository;

    public SectionRepositoryImpl(DbUtils dbUtils, ContentRepository contentRepository) {
        this.dbUtils = dbUtils;
        this.contentRepository = contentRepository;
    }

    @Override
    public List<RebuildSection> createAllSectionsOfCourse(List<Section> sections, int courseId) {

        // 1. 파라미터 검증
        if (sections == null) {
            throw new IllegalArgumentException("sections 가 null 입니다.");
        }
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("sections 가 empty 입니다.");
        }

        // 2. SQL 작성
        StringBuilder sqlBuilder = new StringBuilder();
        String invariant = """
                INSERT INTO section
                (name, seq, course_id)
                VALUES
                """;
        sqlBuilder
                .append(invariant)
                .append("(?, ?, ?),".repeat(sections.size()))
                .deleteCharAt(sqlBuilder.length() - 1);  // 마지막 , 제거

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (1): create - executeUpdate -> t/f (with ResultSet for auto inc key)
            try (PreparedStatement pstmt
                         = conn.prepareStatement(
                                 sqlBuilder.toString(), Statement.RETURN_GENERATED_KEYS)
            ) {

                // SQL 와일드 카드에 값 채우기
                int wildcardSeq = 0;
                for (int i = 0; i < sections.size(); i++) {

                    Section eachSection = sections.get(i);

                    pstmt.setString(++wildcardSeq, eachSection.getName());
                    pstmt.setInt(++wildcardSeq, eachSection.getSeq());
                    pstmt.setInt(++wildcardSeq, courseId);
                }

                // SQL 실행
                int result = pstmt.executeUpdate();
                if (result != sections.size()) {
                    throw new DatabaseException("일부 섹션 생성에 실패했습니다.", null);
                }
                
                // 모든 컨텐츠 저장
                List<Integer> generatedSectionIds = new ArrayList<>();
                Map<Integer, List<Content>> contentsToBeCreated = new HashMap<>();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {

                    for (int i = 0; rs.next(); i++) {
                        int id = rs.getInt(1);
                        generatedSectionIds.add(id);

                        Section eachSection = sections.get(i);
                        contentsToBeCreated.put(id, eachSection.getContents());
                    }
                }
                Map<Integer, List<RebuildContent>> rebuildContentsBySectionId
                        = contentRepository.createAllContentsOfSections(contentsToBeCreated);

                // List<RebuildSection> 객체 생성 및 반환
                List<RebuildSection> rebuildSections = new ArrayList<>();
                for (int i = 0; i < sections.size(); i++) {

                    int sectionId = generatedSectionIds.get(i);
                    List<RebuildContent> rebuildContents = rebuildContentsBySectionId.get(sectionId);

                    rebuildSections.add(new RebuildSection(
                            sectionId,
                            sections.get(i).getSeq(),
                            sections.get(i).getName(),
                            rebuildContents
                    ));
                }

                return rebuildSections;
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        // x. 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
        throw new RuntimeException("CategoryRepositoryImpl.create: 도달 불가능하도록 의도된 지점에 도달했습니다.");
    }

    @Override
    public void deleteAll(Set<Integer> sectionIds) {

        // 1. 파라미터 검증
        if (sectionIds == null || sectionIds.size() == 0) {
            throw new IllegalArgumentException("sectionIds 가 null 이거나 empty 입니다.");
        }

        // 2. SQL 작성
        StringBuilder sql = new StringBuilder(
                """
                DELETE FROM section
                WHERE id IN (
                """
        );
        sql.append(sectionIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "))
        ).append(")");

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (2): update | delete - executeUpdate -> affectedRowCount (no ResultSet)
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

                // SQL 실행
                boolean isNotDeleted = pstmt.executeUpdate() <= 0;
                if (isNotDeleted) {
                    throw new ModificationTargetNotFoundException("삭제할 섹션을 찾을 수 없습니다.", null);
                }
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }
    }

    @Override
    public void updateAllGivenSectionsOfCourse(List<Section> sections, Course originalCourse) {

        // 1. 파라미터 검증
        if (sections == null || sections.isEmpty()) {
            throw new IllegalArgumentException("sections 가 null 또는 empty 입니다.");
        }

        // 2. SQL 작성
        String sql = """
                INSERT INTO section (id, name, seq, course_id)
                VALUES (?, ?, ?, ?)
                AS new_values
                ON DUPLICATE KEY UPDATE
                    id = new_values.id,
                    name = new_values.name,
                    seq = new_values.seq,
                    course_id = new_values.course_id
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (2): update | delete - executeUpdate -> affectedRowCount (no ResultSet)
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                // SQL 와일드 카드에 값 채우기
                for (Section section : sections) {
                    if (section.getId() == null) {
                        pstmt.setNull(1, Types.INTEGER);
                    } else {
                        pstmt.setInt(1, section.getId());
                    }
                    pstmt.setString(2, section.getName());
                    pstmt.setInt(3, section.getSeq());
                    pstmt.setInt(4, originalCourse.getId());

                    // 배치에 추가
                    pstmt.addBatch();
                }

                // 섹션 수정 sql 실행
                int[] updateResults = pstmt.executeBatch();

                // 생성된 섹션 sql 의 자동 생성 키 값 가져오기
                // 이때 자동 생성 키의 조회 순서는, 새로 생성된 레코드에 한하여
                // 위에서 batch 로 추가된 순서를 따릅니다.
                List<Integer> createdSectionIds = new ArrayList<>();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    while (rs.next()) {
                        createdSectionIds.add(rs.getInt(1));
                    }
                }

                // 컨텐츠 삭제
                Set<Long> toBeUpdatedContentIds = new HashSet<>();
                for (Section section : sections) {
                    for (Content content : section.getContents()) {
                        if (content.getId() != null) {
                            toBeUpdatedContentIds.add(content.getId());
                        }
                    }
                }
                Set<Long> toBeDeletedContentIds = new HashSet<>();
                for (Section section : originalCourse.sections()) {
                    for (Content content : section.getContents()) {
                        toBeDeletedContentIds.add(content.getId());
                    }
                }
                toBeDeletedContentIds.removeAll(toBeUpdatedContentIds);

                if (!toBeDeletedContentIds.isEmpty()) {
                    contentRepository.deleteAll(toBeDeletedContentIds);
                }

                // 컨텐츠 수정
                // SectionId -> contents
                Map<Integer, List<Content>> contentsBySectionId = new HashMap<>();
                int idxCreatedSectionIds = 0;
                for (Section section : sections) {
                    int curSectionId = -1;
                    if (section.getId() == null) {
                        curSectionId
                                = createdSectionIds.get(idxCreatedSectionIds++);
                    } else {
                        curSectionId = section.getId();
                    }
                    contentsBySectionId.put(curSectionId, section.getContents());
                }
                contentRepository.updateAllContentsOfSections(contentsBySectionId, originalCourse);
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }
    }
}
