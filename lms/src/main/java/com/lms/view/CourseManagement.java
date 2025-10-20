package com.lms.view;

import com.lms.domain.category.Category;
import com.lms.domain.course.Course;
import com.lms.domain.course.spec.creation.CreateContent;
import com.lms.domain.course.spec.creation.CreateCourse;
import com.lms.domain.course.spec.creation.CreateSection;
import com.lms.domain.course.spec.rebuild.RebuildContent;
import com.lms.domain.course.spec.rebuild.RebuildCourse;
import com.lms.domain.course.spec.rebuild.RebuildSection;
import com.lms.repository.course.dto.CourseInfo;
import com.lms.repository.course.dto.CourseInfoSearchFilter;
import com.lms.repository.course.dto.KeywordSearchScope;
import com.lms.service.CourseService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

// 이름, 소속 카테고리, 개요, detail, 작성자id, 생성 일시, 수정 일시
// 섹션 설정 (이름, 섹션 순서, 소속 강의id)
public class CourseManagement {
    private final CourseService courseService = new CourseService();

    public void showCourseByCategoryId(Scanner scanner, int categoryId) throws SQLException {
        System.out.println("\n=== 코스 목록 ===");
        Course course = courseService.findCourseById(categoryId);  // 카테고리 id로 카테고리 목록 조회
        System.out.println("ID: " + course.getId() + "Title: " + course.getTitle());
    }



    public void searchCourseWithFilter(Scanner scanner) {
        System.out.println("=== 코스 검색 ===");

        // 1. 키워드 입력
        System.out.print("검색 키워드 입력 (없으면 Enter): ");
        String keyword = scanner.nextLine().trim();
        if (keyword.isBlank()) keyword = null;

        // 2. 키워드 검색 범위 선택
        System.out.println("키워드 검색 범위 선택:");
        System.out.println("1. TITLE");
        System.out.println("2. TITLE + SUMMARY");
        System.out.println("3. TITLE + SUMMARY + DETAIL");
        System.out.print("선택 (기본: 3): ");
        String scopeInput = scanner.nextLine();

        KeywordSearchScope keywordSearchScope = KeywordSearchScope.TITLE_SUMMARY_DETAIL; // 기본값
        switch (scopeInput) {
            case "1" -> keywordSearchScope = KeywordSearchScope.TITLE;
            case "2" -> keywordSearchScope = KeywordSearchScope.TITLE_SUMMARY;
            case "3", "" -> keywordSearchScope = KeywordSearchScope.TITLE_SUMMARY_DETAIL;
            default -> System.out.println("잘못된 입력입니다. 기본값(TITLE_SUMMARY_DETAIL)으로 설정됩니다.");
        }

        // 3. 날짜 필터 (옵션)
        LocalDate createdFrom = inputDate(scanner, "생성일 (부터, yyyy-MM-dd, 생략 가능): ");
        LocalDate createdTo = inputDate(scanner, "생성일 (까지, yyyy-MM-dd, 생략 가능): ");
        LocalDate updatedFrom = inputDate(scanner, "수정일 (부터, yyyy-MM-dd, 생략 가능): ");
        LocalDate updatedTo = inputDate(scanner, "수정일 (까지, yyyy-MM-dd, 생략 가능): ");

        // 4. 작성자 이름 필터
        System.out.print("작성자 이름 (없으면 Enter): ");
        String userName = scanner.nextLine().trim();
        if (userName.isBlank()) userName = null;

        // 5. 카테고리 필터 (미구현: 빈 리스트로)
        List<Category> categorySearchScope = new ArrayList<>();

        // 6. 페이징 입력
        System.out.print("페이지 번호 (기본: 1): ");
        String pageNumInput = scanner.nextLine();
        Integer pageNum = pageNumInput.isBlank() ? 1 : Integer.parseInt(pageNumInput);

        System.out.print("페이지 크기 (기본: 10): ");
        String pageSizeInput = scanner.nextLine();
        Integer pageSize = pageSizeInput.isBlank() ? 10 : Integer.parseInt(pageSizeInput);

        // 7. 필터 객체 생성
        CourseInfoSearchFilter filter = new CourseInfoSearchFilter(
                keyword,
                keywordSearchScope,
                categorySearchScope,
                createdFrom,
                createdTo,
                updatedFrom,
                updatedTo,
                userName,
                pageSize,
                pageNum
        );

        // 8. 검색 수행
        try {
            List<CourseInfo> results = courseService.searchCourses(filter);

            System.out.println("\n=== 검색 결과 ===");
            if (results.isEmpty()) {
                System.out.println("결과가 없습니다.");
            } else {
                for (CourseInfo course : results) {
                    System.out.printf("ID: %d | 제목: %s | 요약: %s | 작성자: %s | 생성일: %s%n",
                            course.id(), course.title(), course.summary(), course.userId(), course.createdAt());
                }
            }
        } catch (Exception e) {
            System.out.println("검색 중 오류 발생: " + e.getMessage());
        }
    }

    // 날짜 입력을 처리하는 유틸 메서드
    private LocalDate inputDate(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        if (input.isBlank()) return null;

        try {
            return LocalDate.parse(input);
        } catch (Exception e) {
            System.out.println("날짜 형식이 올바르지 않습니다. 생략 처리됩니다.");
            return null;
        }
    }


    public int selectCourse(Scanner scanner) throws SQLException {
        System.out.print("상세 메뉴로 들어갈 코스 ID 입력: ");
        int courseId = scanner.nextInt();
        scanner.nextLine();

        return courseId;
    }

    public void showCourseDetail(Scanner scanner, int courseId) {
        Course course = courseService.findCourseById(courseId); // 코스 정보 조회

        System.out.println("=== " + course.getTitle() + " 코스 조회 ===");
        System.out.println(course.getId());
        System.out.println(course.getDetail());
    }


    public void addCourse(Scanner scanner, int categoryId) {
        System.out.println("=== 코스 등록 ===");

        // 1. 기본 코스 정보 입력
        System.out.print("코스 제목: ");
        String title = scanner.nextLine();

        System.out.print("코스 요약: ");
        String summary = scanner.nextLine();

        System.out.print("코스 상세 설명: ");
        String detail = scanner.nextLine();

        Integer subCategoryId = categoryId;

        System.out.print("작성자 ID 입력: ");
        Long userId = Long.parseLong(scanner.nextLine());

        // 2. 섹션 개수 입력
        System.out.print("등록할 섹션 개수: ");
        int sectionCount = Integer.parseInt(scanner.nextLine());

        List<CreateSection> sectionList = new ArrayList<>();

        for (int i = 0; i < sectionCount; i++) {
            System.out.println("[" + (i + 1) + "번째 섹션]");

            System.out.print("섹션 순서 (seq): ");
            int sectionSeq = Integer.parseInt(scanner.nextLine());

            System.out.print("섹션 이름: ");
            String sectionName = scanner.nextLine();

            System.out.print("섹션에 포함할 콘텐츠 개수: ");
            int contentCount = Integer.parseInt(scanner.nextLine());

            List<CreateContent> contentList = new ArrayList<>();

            for (int j = 0; j < contentCount; j++) {
                System.out.println("  [" + (j + 1) + "번째 콘텐츠]");

                System.out.print("  콘텐츠 이름: ");
                String contentName = scanner.nextLine();

                System.out.print("  콘텐츠 순서 (seq): ");
                int contentSeq = Integer.parseInt(scanner.nextLine());

                System.out.print("  콘텐츠 내용 (body): ");
                String contentBody = scanner.nextLine();

                contentList.add(new CreateContent(contentName, contentSeq, contentBody));
            }

            sectionList.add(new CreateSection(sectionSeq, sectionName, contentList));
        }

        // 3. 최종 DTO 생성
        CreateCourse courseSpec = new CreateCourse(
                title,
                summary,
                detail,
                subCategoryId,
                userId,
                sectionList
        );

        // 4. 서비스 호출
        courseService.createCourse(courseSpec);
        System.out.println("코스 등록 완료!");
    }

    public void updateCourse(Scanner scanner, int courseId) throws SQLException {
        System.out.println("=== 코스 수정 ===");

        Course course = courseService.findCourseById(courseId);

        // 기존 값
        String title = course.getTitle();
        String summary = course.getSummary();
        String detail = course.getDetail();
        Integer subCategoryId = course.getSubCategoryId();

        System.out.println("공란 입력 시 기존 값 유지");
        System.out.print("제목 수정: ");
        String inputTitle = scanner.nextLine();
        if (!inputTitle.isBlank()) {
            title = inputTitle;
        }

        System.out.print("요약 수정: ");
        String inputSummary = scanner.nextLine();
        if (!inputSummary.isBlank()) {
            summary = inputSummary;
        }

        System.out.print("상세 설명 수정: ");
        String inputDetail = scanner.nextLine();
        if (!inputDetail.isBlank()) {
            detail = inputDetail;
        }

        System.out.print("서브 카테고리 ID 수정: ");
        String inputSubCategoryId = scanner.nextLine();
        if (!inputSubCategoryId.isBlank()) {
            subCategoryId = Integer.parseInt(inputSubCategoryId);
        }

        // Section과 Content를 직접 Rebuild (toRebuild 메서드 없이)
        List<RebuildSection> rebuildSections = course.sections().stream()
                .map(section -> new RebuildSection(
                        section.getId(),
                        section.getSeq(),
                        section.getName(),
                        section.getContents().stream()
                                .map(content -> new RebuildContent(
                                        content.getId(),
                                        content.getName(),
                                        content.getSeq(),
                                        content.getBody()
                                ))
                                .toList()
                ))
                .toList();

        // RebuildCourse 생성
        RebuildCourse rebuildCourse = new RebuildCourse(
                course.getId(),
                title,
                summary,
                detail,
                subCategoryId,
                course.getUserId(),
                rebuildSections,
                course.getCreatedAt(),
                course.getUpdatedAt()

        );

        // 수정된 course 객체 재생성
        Course updatedCourse = Course.rebuild(rebuildCourse);

        // 서비스 계층에 수정 요청
        courseService.updateCourse(updatedCourse);

        System.out.println("코스 수정 완료!");
    }

    public void deleteCourse(Scanner scanner, int courseId) {
        System.out.println("=== 코스 삭제 ===");

        try {
            courseService.deleteCourse(courseId);  // 실제 삭제 호출
            System.out.println("코스가 성공적으로 삭제되었습니다.");

        } catch (InputMismatchException e) {
            System.out.println("유효한 숫자를 입력해주세요.");
            scanner.nextLine(); // 버퍼 클리어
        } catch (Exception e) {
            System.out.println("코스 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}