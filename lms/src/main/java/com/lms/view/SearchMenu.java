package com.lms.view;

import java.sql.SQLException;
import java.util.Scanner;

public class SearchMenu {
    CategoryManagement categoryManagement = new CategoryManagement();
    CourseManagement courseManagement = new CourseManagement();
    CourseMenu courseMenu = new CourseMenu();

    public void showSearchMenu(Scanner scanner) throws SQLException {

        while (true) {
            System.out.println("1. 카테고리 조회");
            System.out.println("2. 강좌 검색");
            System.out.println("3. 뒤로가기");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1:
                    categoryManagement.showCategoryLevelOne(scanner);   // 상위 카테고리 조회
                    courseMenu.searchCourseByCategory(scanner); // 카테고리 id 입력 후 해당 카테고리 코스 추가 및 코스 상세 메뉴 진입
                    break;
                case 2:
                    courseManagement.searchCourseWithFilter(scanner);    // 강좌 검색 후 관련 강좌 목록 조회
                    courseManagement.selectCourse(scanner);
                    break;
                case 3:
                    return;
            }
        }
    }
}
