package com.lms.view;

import java.sql.SQLException;
import java.util.Scanner;

public class SearchMenu {
    private final CategoryManagement categoryManagement = new CategoryManagement();
    private final CourseManagement courseManagement = new CourseManagement();
    private final CourseMenu courseMenu = new CourseMenu();

    public void showSearchMenu(Scanner scanner) throws SQLException {

        while (true) {
            System.out.println("1. 카테고리 조회");
            System.out.println("2. 강좌 검색");
            System.out.println("3. 뒤로가기");
            System.out.print("메뉴 선택: ");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1 -> {
                    categoryManagement.showCategoryLevelOne(scanner);
                    categoryManagement.showCategoryLevelTwo(scanner);
                    courseMenu.searchCourseByCategory(scanner);
                }
                case 2 -> {
                    courseManagement.searchCourseWithFilter(scanner);
                    int inputCourseId = courseManagement.selectCourse(scanner);
                    courseMenu.showCourseMenu(scanner, inputCourseId);
                }
                case 3 -> {
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }
}