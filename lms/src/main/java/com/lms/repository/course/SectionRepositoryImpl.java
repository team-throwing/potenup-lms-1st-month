package com.lms.repository.course;

import com.lms.domain.course.Content;
import com.lms.domain.course.Section;
import com.lms.domain.course.spec.rebuild.RebuildContent;
import com.lms.domain.course.spec.rebuild.RebuildSection;
import com.lms.repository.exception.DatabaseException;
import com.lms.repository.exception.converter.DbUtils;
import com.lms.service.ConnectionHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .append("(?, ?, ?)".repeat(sections.size()));

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

                        rs.next();
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
}
