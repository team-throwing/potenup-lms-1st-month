package com.lms.view;

import java.sql.SQLException;
import java.util.Scanner;

public class CourseMenu {
    CourseManagement courseManagement = new CourseManagement();
    SectionMenu sectionMenu = new SectionMenu();
    MainMenu mainMenu = new MainMenu();

    public void showCourseMenu(Scanner scanner, int courseId) throws SQLException {

        while (true) {
            System.out.println("1. 코스 보기");
            System.out.println("2. 코스 수정");
            System.out.println("3. 코스 삭제");
            System.out.println("4. 섹션 관리");
            System.out.println("5. 메인 메뉴로");
            System.out.print("메뉴 입력: ");
            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1:
                    courseManagement.showCourseDetail(scanner, courseId);
                    break;
                case 2:
                    courseManagement.updateCourse(scanner, courseId);
                    break;
                case 3:
                    courseManagement.deleteCourse(scanner, courseId);
                    break;
                case 4:
                    sectionMenu.showSectionMenu(scanner, courseId);
                    break;
                case 5:
                    mainMenu.showMainMenu(scanner);
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    public void  searchCourseByCategory(Scanner scanner) throws SQLException {
        System.out.print("=== 카테고리 id 입력: ");
        int categoryId = scanner.nextInt();
        scanner.nextLine();

        while (true) {
            System.out.println("1. 카테고리 내 코스 추가");
            System.out.println("2. 코스 상세 메뉴");
            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1:
                    courseManagement.addCourse(scanner, categoryId);
                    break;
                case 2:
                    courseManagement.selectCourse(scanner);
                    break;
            }
        }
    }
}
