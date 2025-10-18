package com.lms.view;

import com.lms.domain.category.CategoryLevel;

import java.util.Scanner;

public class SearchMenu {
    Scanner scanner = new Scanner(System.in);

    public void showSearchMenu() {
//        CategoryService categoryService = new CategoryService();
//        CourseService courseService = new CourseService();

        CategoryLevel categoryLevel = CategoryLevel.ONE;

        while (true) {
        // 최상위 카테고리 목록 출력
        System.out.println("1. 카테고리 목록 보기");

        System.out.println("2. 강좌 검색");

        int userInput = scanner.nextInt();
        scanner.nextLine();

        switch (userInput) {
            case 1:
                System.out.println("=== 상위 카테고리 목록 ===");
//                List<Category> categories1 = categoryService.findAllByCategoryLevel(categoryLevel);
//                for (Category category : categories1) {
//                    System.out.println("ID: " + category.getId() + "Name: " + category.getName());
//                }

                System.out.println("=== 카테고리 선택 ===");
                System.out.println("취소하려면 0을 입력하세요.");
                System.out.print("카테고리 선택(id): ");

                int inputCategoryId1 = scanner.nextInt();
                scanner.nextLine();

                if (inputCategoryId1 == 0) {
                    System.out.println("카테고리 선택을 취소합니다.");
                    break;
                }

                System.out.println("\n=== 하위 카테고리 목록 ===");
//                List<Category> categories2 = categoryService.findAllByCategoryLevel(CategoryLevel.TWO);
//                for (Category category : categories2) {
//                    System.out.println("ID: " + category.getId() + "Name: " + category.getName());
//                }

                System.out.println("=== 카테고리 선택 ===");
                System.out.println("취소하려면 0을 입력하세요.");
                System.out.print("카테고리 선택(id): ");

                int inputCategoryId2 = scanner.nextInt();
                scanner.nextLine();

                if (inputCategoryId2 == 0) {
                    System.out.println("카테고리 입력을 취소합니다.");
                    break;
                }

                System.out.println("=== " + inputCategoryId2 + "카테고리의 코스 목록 출력 ===");
//                List<Course> categoryCourses = courseService.findByCategoryId(inputCategoryId2);
//                for (Course course : categoryCourses) {
//                    System.out.println("ID: " + course.getId() + "title: " + course.getTitle());
//                }
//                break;

            System.out.println("=== 코스 선택 ===");
            System.out.println("취소하려면 0을 입력하세요.");
            System.out.println("코스 선택(id): ");

            int inputCourseId = scanner.nextInt();
            scanner.nextLine();

            if (inputCourseId == 0) {
                System.out.println("코스 선택 취소");
                break;
            }

            new CourseManagement().showCourseMenu(inputCourseId);
            case 2:
                System.out.println("=== 강좌 검색 ===");
                System.out.println("취소하려면 0을 입력하세요.");
                System.out.print("강좌 검색(제목): ");
                String keyword = scanner.nextLine();

                if (keyword.equals("0")) {
                    System.out.println("강좌 검색을 취소합니다.");
                    break;
                }

//                List<Course> courses = CourseService.searchCourseInfo();
//                for (Course course : courses) {
//                    System.out.println("ID: " + course.getId() + "title: " + course.getTitle());
//                }

                System.out.println("\n=== 코스 선택 ===");
                System.out.println("취소하려면 0을 입력하세요.");

                int searchCourseId = scanner.nextInt();
                scanner.nextLine();

                if (searchCourseId == 0) {
                    System.out.println("코스 선택을 취소합니다.");
                    break;
                }

                new CourseManagement().showCourseMenu(searchCourseId);
                break;
            }
        }
    }


    public void showFirstCategory() {

    }

    public void showSecondCategory() {

    }
}
