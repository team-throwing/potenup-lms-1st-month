package com.lms.view;

import java.sql.SQLException;
import java.util.Scanner;

public class CourseMenu {
    private final CourseManagement courseManagement = new CourseManagement();
    private final SectionMenu sectionMenu = new SectionMenu();
    private final NoticeMenu noticeMenu = new NoticeMenu();
    private final CourseMenu courseMenu = new CourseMenu();

    public void showCourseMenu(Scanner scanner, int courseId) throws SQLException {

        while (true) {
            System.out.println("1. 코스 보기");
            System.out.println("2. 코스 수정");
            System.out.println("3. 코스 삭제");
            System.out.println("4. 섹션 관리");
            System.out.println("5. 코스 공지 관리");
            System.out.println("6. 뒤로가기");
            System.out.print("메뉴 입력: ");
            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1 -> courseManagement.showCourseDetail(scanner, courseId);
                case 2 -> courseManagement.updateCourse(scanner, courseId);
                case 3 -> courseManagement.deleteCourse(scanner, courseId);
                case 4 -> sectionMenu.showSectionMenu(scanner, courseId);
                case 5 -> noticeMenu.showNoticeMenu(scanner, courseId);
                case 6 -> {
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    public void searchCourseByCategory(Scanner scanner) throws SQLException {
        System.out.print("조회할 카테고리 id 입력: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine();

        while (true) {
            System.out.println("=== 코스 제어 메뉴 ===");
            System.out.println("\n1. 카테고리 내 코스 목록 조회");
            System.out.println("2. 카테고리 내 코스 추가");
            System.out.println("3. 코스 상세 메뉴");
            System.out.println("4. 뒤로가기");
            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1 -> courseManagement.showCourseByCategoryId(scanner, categoryId);

                case 2 -> courseManagement.addCourse(scanner, categoryId);
                case 3 -> {
                    int inputCourseId = courseManagement.selectCourse(scanner);
                    courseMenu.showCourseMenu(scanner, inputCourseId);
                }
                case 4 -> {
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }
}
