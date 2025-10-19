package com.lms.view;

import java.util.Scanner;

public class SearchMenu {
    private final CategoryManagement  categoryManagement;
//    private final CourseManagement  courseManagement;

    public SearchMenu(CategoryManagement categoryManagement) {
        this.categoryManagement = categoryManagement;
    }

//    public SearchMenu(CategoryManagement categoryManagement, CourseManagement courseManagement) {
//        this.categoryManagement = categoryManagement;
//        this.courseManagement = courseManagement;
//    }

    public void showSearchMenu(Scanner scanner) {

        while (true) {
            // 최상위 카테고리 목록 출력
            System.out.println("1. 카테고리 조회");
            System.out.println("2. 강좌 검색");
            System.out.println("3. 뒤로가기");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1:
                    categoryManagement.showCategoryLevelOne(scanner);   // 상위 카테고리 목록 조회
                    categoryManagement.showCategoryLevelTwo(scanner);   // 부모 카테고리 ID로 자식 카테고리 목록 조회
//                    courseManagement.showCourseByCategoryId(scanner); // 자식 카테고리 ID에 있는 강좌 목록 조회
//                    courseManagement.showCourseMenu(scanner);         // 강좌 id를 입력해서 해당 코스 메뉴로 진입
                    break;
                case 2:
//                    courseManagement.showCourseByKeyword(scanner);    // 강좌 검색 후 관련 강좌 목록 조회
//                    courseManagement.showCourseMenu(scanner);         // 강좌 id를 입력해서 해당 강좌 메뉴로 진입
                    break;
                case 3:
                    return;
            }
        }
    }
}
