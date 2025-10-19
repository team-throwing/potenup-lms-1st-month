package com.lms.repository.course;

import com.lms.domain.course.Content;
import com.lms.domain.course.Course;
import com.lms.domain.course.Section;
import com.lms.domain.course.spec.creation.CreateContent;
import com.lms.domain.course.spec.creation.CreateCourse;
import com.lms.domain.course.spec.creation.CreateSection;
import com.lms.repository.config.DataSourceFactory;
import com.lms.repository.config.RepositoryConfig;
import com.lms.repository.course.dto.CourseInfo;
import com.lms.repository.course.dto.CourseInfoSearchFilter;
import com.lms.service.ConnectionHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

class CourseRepositoryTest {

    Connection conn;

    private final CourseRepository courseRepository
            = RepositoryConfig.courseRepository();

    @BeforeEach
    void startTransaction() throws SQLException {
        conn = DataSourceFactory.get().getConnection();
        ConnectionHolder.set(conn);
        conn.setAutoCommit(false);
    }

    @AfterEach
    void closeConnection() throws SQLException {
        conn.close();
    }

    void rollback(SQLException e, Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        System.err.println("==================");
        e.printStackTrace();
    }

    @Test
    void create() {
        try {

            // 컨텐츠 생성
            List<CreateContent> createContents1 = new ArrayList<>();
            createContents1.add(new CreateContent(
                    "컨텐츠 1-1", 1, "......"
            ));
            createContents1.add(new CreateContent(
                    "컨텐츠 1-2", 2, "......"
            ));

            List<CreateContent> createContents2 = new ArrayList<>();
            createContents2.add(new CreateContent(
                    "컨텐츠 2-1", 1, "......"
            ));
            createContents2.add(new CreateContent(
                    "컨텐츠 2-2", 2, "......"
            ));
            createContents2.add(new CreateContent(
                    "컨텐츠 2-3", 3, "......"
            ));

            List<CreateContent> createContents3 = new ArrayList<>();
            createContents3.add(new CreateContent(
                    "컨텐츠 3-1", 1, "......"
            ));
            createContents3.add(new CreateContent(
                    "컨텐츠 3-2", 2, "......"
            ));

            // 섹션 생성
            List<CreateSection> createSections = new ArrayList<>();
            createSections.add(
                    new CreateSection(
                            1, "섹션 1", createContents1
                    )
            );
            createSections.add(
                    new CreateSection(
                            2, "섹션 2", createContents2
                    )
            );
            createSections.add(
                    new CreateSection(
                            3, "섹션 3", createContents3
                    )
            );

            // 강좌 생성
            CreateCourse createCourse = new CreateCourse(
                    "새 강좌 타이틀",
                    "새 강좌 요약요약요약요약요약",
                    "새 강좌 상세 설명설명설명설명",
                    1,
                    1001L,
                    createSections
            );

            Course created = courseRepository.create(
                    Course.create(createCourse)
            );

            printCourse(created);

            conn.commit();
        } catch (SQLException e) {
            rollback(e, conn);
        }
    }

    @Test
    void findById() {
        try {

            int courseIdToFind = 132;
            Optional<Course> optionalFound = courseRepository.findById(courseIdToFind);
            if (optionalFound.isEmpty()) {
                System.err.println("결과 없음!!! 테스트 데이터 중 적당히 있는거로 전달할 것!");
                fail();
            }
            Course found = optionalFound.get();

            printCourse(found);

            conn.commit();
        } catch (SQLException e) {
            rollback(e, conn);
        }
    }

    @Test
    void searchCourseInfo() {
        try {

            // 아직 구현 안 됨
            CourseInfoSearchFilter filter = CourseInfoSearchFilter.builder()
//                    .keyword("마스터")
//                    .pageSize(5)
//                    .pageNum(3)
                    .build();
            List<CourseInfo> searchResult = courseRepository.searchCourseInfo(filter);
            searchResult.forEach(System.out::println);

            conn.commit();
        } catch (SQLException e) {
            rollback(e, conn);
        }
    }

    @Test
    void update() {
        try {

            // 가져오기
            int courseIdToFind = 132;
            Optional<Course> optionFound = courseRepository.findById(courseIdToFind);
            if (optionFound.isEmpty()) {
                System.out.println("id 가 " + courseIdToFind + " ");
                fail();
            }
            Course found = optionFound.get();

            System.out.println("**********************");
            System.out.println("*       수정 전       *");
            System.out.println("**********************");
            System.out.println();

            printCourse(found);

            System.out.println();
            System.out.println("**********************");
            System.out.println("*       수정 후       *");
            System.out.println("**********************");

            // 앞서 create 테스트 메서드에서 생성했던
            // 3번째 섹션의 2번째 컨텐츠 제거
            found.deleteContent(10033L, 1048);
            // 2번째 섹션에 컨텐츠 추가
            found.addContent(
                    new CreateContent(
                            "컨텐츠 2-4",
                            4,
                            "......"
                    ),
                    1047
            );

            // 업데이트
            courseRepository.update(found);

            // 업데이트 확인
            optionFound = courseRepository.findById(courseIdToFind);
            if (optionFound.isEmpty()) {
                System.out.println("id 가 " + courseIdToFind + " ");
                fail();
            }
            found = optionFound.get();

            printCourse(found);

            conn.commit();
        } catch (SQLException e) {
            rollback(e, conn);
        }
    }

    @Test
    void delete() {
        try {

            int courseIdToFind = 131;
            Optional<Course> found = courseRepository.findById(courseIdToFind);
            if (found.isEmpty()) {
                System.out.println("id 가 " + courseIdToFind + " 인 강좌를 찾을 수 없습니다.");
                fail();
            }

            courseRepository.delete(courseIdToFind);

            Optional<Course> foundAfterDeleted = courseRepository.findById(courseIdToFind);
            if (foundAfterDeleted.isPresent()) {
                System.out.println(courseIdToFind + "에 해당하는 강좌가 삭제되지 않았습니다.");
                fail();
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(e, conn);
        }
    }

    private static void printCourse(Course course) {
        StringBuilder toBePrinted = new StringBuilder(
                "Course={" +
                    "\n\tid=" + course.getId() +
                    "\n\ttitle='" + course.getTitle() + '\'' +
                    "\n\tsummary='" + course.getTitle() + '\'' +
                    "\n\tdetail='" + course.getDetail() + '\'' +
                    "\n\tsubCategoryId=" + course.getSubCategoryId() +
                    "\n\tuserId=" + course.getUserId() +
                    "\n\tcreatedAt=" + course.getCreatedAt() +
                    "\n\tupdatedAt=" + course.getUpdatedAt() +
                    "\n\tsections={"
        );

        for (Section section : course.sections()) {
            toBePrinted.append(
                        "\n\t\t{" +
                            "\n\t\t\tid=" + section.getId() +
                            "\n\t\t\tname='" + section.getName() + '\'' +
                            "\n\t\t\tseq=" + section.getSeq() +
                            "\n\t\t\tcontents={"
            );

            for (Content content : section.getContents()) {
                toBePrinted.append(
                                "\n\t\t\t\t{" +
                                    "\n\t\t\t\t\tid=" + content.getId() +
                                    "\n\t\t\t\t\tname='" + content.getName() + '\'' +
                                    "\n\t\t\t\t\tseq=" + content.getSeq() +
                                    "\n\t\t\t\t\tbody='" + content.getBody() + '\'' +
                                "\n\t\t\t\t},"
                );
            }
            toBePrinted.append(
                            "\n\t\t\t},");

            toBePrinted.append(
                        "\n\t\t},");
        }

        toBePrinted.append(
                "\n\t}");

        toBePrinted.append(
                "\n}");

        System.out.println(toBePrinted);
    }
}