package com.lms.repository.course;

import com.lms.domain.course.Content;
import com.lms.domain.course.Course;
import com.lms.domain.course.spec.rebuild.RebuildContent;
import com.lms.repository.exception.DatabaseException;
import com.lms.repository.exception.ModificationTargetNotFoundException;
import com.lms.repository.exception.converter.DbUtils;
import com.lms.service.ConnectionHolder;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class ContentRepositoryImpl implements ContentRepository {

    private final DbUtils dbUtils;

    public ContentRepositoryImpl(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    @Override
    public Map<Integer, List<RebuildContent>> createAllContentsOfSections(
            Map<Integer, List<Content>> contentsBySectionId) {

        // 1. 파라미터 검증
        if (contentsBySectionId == null) {
            throw new IllegalArgumentException("contentsBySectionId 가 null");
        }

        // 2. SQL 작성
        int numOfContents = 0;
        for (List<Content> contentList : contentsBySectionId.values()) {
            numOfContents += contentList.size();
        }
        StringBuilder sqlBuilder = new StringBuilder();
        String invariant = """
                INSERT INTO content
                (name, seq, section_id, body)
                VALUES
                """;
        sqlBuilder
                .append(invariant)
                .append("(?, ?, ?, ?),".repeat(numOfContents))
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
                for (Integer sectionId : contentsBySectionId.keySet()) {
                    List<Content> contents = contentsBySectionId.get(sectionId);

                    if (contents == null) {
                        continue;
                    }

                    for (Content content : contents) {
                        pstmt.setString(++wildcardSeq, content.getName());
                        pstmt.setInt(++wildcardSeq, content.getSeq());
                        pstmt.setInt(++wildcardSeq, sectionId);
                        pstmt.setString(++wildcardSeq, content.getBody());
                    }
                }

                // SQL 실행
                int result = pstmt.executeUpdate();
                if (result != numOfContents) {
                    throw new DatabaseException("일부 컨텐츠 생성에 실패했습니다.", null);
                }

                // Map<Integer, List<RebuildContent>> 객체 생성 및 반환
                Map<Integer, List<RebuildContent>> rebuildContentsBySectionId
                        = new HashMap<>();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {

                    for (Integer sectionId : contentsBySectionId.keySet()) {
                        List<RebuildContent> rebuildContents = new ArrayList<>();
                        List<Content> contents = contentsBySectionId.get(sectionId);

                        if (contents == null) {
                            continue;
                        }

                        for (Content content : contents) {
                            rs.next();
                            rebuildContents.add(new RebuildContent(
                                    rs.getLong(1),
                                    content.getName(),
                                    content.getSeq(),
                                    content.getBody()
                            ));
                        }
                        rebuildContentsBySectionId.put(sectionId, rebuildContents);
                    }
                }

                return rebuildContentsBySectionId;
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        // x. 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
        throw new RuntimeException("CategoryRepositoryImpl.create: 도달 불가능하도록 의도된 지점에 도달했습니다.");
    }

    @Override
    public void deleteAll(Set<Long> contentIds) {

        // 1. 파라미터 검증
        if (contentIds == null || contentIds.isEmpty()) {
            throw new IllegalArgumentException("contentIds 가 null 또는 empty");
        }

        // 2. SQL 작성
        StringBuilder sql = new StringBuilder(
                """
                DELETE FROM content
                WHERE id IN (
                """
        );
        sql.append(contentIds.stream()
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
                boolean isNotCompleted = pstmt.executeUpdate() <= 0;
                if (isNotCompleted) {
                    throw new ModificationTargetNotFoundException("삭제할 컨텐츠를 찾을 수 없습니다.", null);
                }
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }
    }

    @Override
    public void updateAllContentsOfSections(
            Map<Integer, List<Content>> contentsBySectionId, Course originalCourse) {

        // 1. 파라미터 검증
        if (contentsBySectionId == null) {
            throw new IllegalArgumentException("contentsBySectionId 가 null");
        }

        // 2. SQL 작성
        String sql = """
                INSERT INTO content (id, name, seq, section_id, body)
                VALUES (?, ?, ?, ?, ?)
                AS new_values
                ON DUPLICATE KEY UPDATE
                    id = new_values.id,
                    name = new_values.name,
                    seq = new_values.seq,
                    section_id = new_values.section_id,
                    body = new_values.body
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (2): update | delete - executeUpdate -> affectedRowCount (no ResultSet)
            try (PreparedStatement pstmt = conn.prepareStatement(sql /*, Statement.RETURN_GENERATED_KEYS*/)) {

                // SQL 와일드 카드에 값 채우기
                for (int sectionId : contentsBySectionId.keySet()) {
                    List<Content> contents = contentsBySectionId.get(sectionId);

                    if (contents == null) {
                        continue;
                    }

                    for (Content content : contents) {
                        if (content.getId() == null) {
                            pstmt.setNull(1, Types.INTEGER);
                        } else {
                            pstmt.setLong(1, content.getId());
                        }
                        pstmt.setString(2, content.getName());
                        pstmt.setInt(3, content.getSeq());
                        pstmt.setInt(4, sectionId);
                        pstmt.setString(5, content.getBody());

                        // 배치에 추가
                        pstmt.addBatch();
                    }
                }

                // SQL 실행
                int[] updateResults = pstmt.executeBatch();
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }
    }
}
