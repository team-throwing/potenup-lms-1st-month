package com.lms.view;

import com.lms.domain.category.Category;
import com.lms.domain.category.CategoryLevel;

import java.util.List;
import java.util.Scanner;

// 이름, 부모 카테고리, 카테고리 레벨
public class CategoryMenu {
    private Scanner scanner = new Scanner(System.in);

    public void showCategoryMenu0() {
        CategoryLevel categoryLevel = CategoryLevel.ONE;

        while (true) {
            System.out.println("\n=== 상위 카테고리 메뉴 ===");

            System.out.println("1. 상위 카테고리 목록");
            // id로 조회? , name으로 조회? (전페 조회)
            // level 0 이 아닌 카테고리 id 값이 들어오면?

            // user 확인?
            // level 0 카테고리에 등록
            System.out.println("2. 상위 카테고리 등록");

            // level 0 카테고리 조회
            System.out.println("3. 상위 카테고리 조회");
            // 조회 카테고리 내 level1 카테고리 목록 확인
            // 이름으로 상위 카테고리 조회?

            // 상위 카테고리 수정
            System.out.println("4. 상위 카테고리 수정");
            // level 0이 아닌 카테고리 id 값이 들어오면?

            System.out.println("5. 상위 카테고리 삭제");

            // 선택한 level0 카테고리 페이지 입장
            System.out.println("6. 상위 카테고리 선택");
            // 해당 카테고리 밑에 있는 하위 카테고리 목록

            System.out.println("7. 뒤로가기");
            System.out.println("0. 프로그램 종료");
            System.out.print("카테고리 메뉴 선택: ");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            if (userInput == 0) {
                System.exit(0);
            }

            switch (userInput) {
                case 1:
                    System.out.println("=== 상위 카테고리 목록 ===");

                    //TODO level ONE category 출력, 서비스 확인 필요
//                    List<Category> categories = CategoryService.findAllCategory(categoryLevel);
//
//                    for (Category category : categories) {
//                        System.out.println("ID: " + category.getId() + "이름: " + category.getName());
//                    }
                    break;
                case 2:
                    System.out.println("=== 카테고리 등록 ===");
                    System.out.println("취소하려면 0을 입력하세요.");
                    System.out.print("등록할 카테고리 이름: ");
                    String categoryName = scanner.nextLine();

                    if (categoryName.equals("0")) {
                        System.out.println("카테고리 등록이 취소되었습니다.");
                        break;
                    }


                    //TODO: 서비스 및 DTO 확인 필요
//                    CategoryRequestDTO dto = new CategoryRequestDTO(
//                            categoryName,
//                            categoryLevel
//                    );

//                    CategoryService.addCategory(dto);
                    break;
                case 3:
                    System.out.println("=== 카테고리 조회 ===");
                    System.out.println("취소하려면 0을 입력하세요.");
                    System.out.print("선택 카테고리 id: ");

                    long inputParentCategoryId = scanner.nextInt();
                    scanner.nextLine();

                    if (inputParentCategoryId == 0) {
                        System.out.println("카테고리 등록이 취소되었습니다.");
                        break;
                    }

                    // 카테고리 테이블에서 ParentCategoryID가 X인 것들을 모아본다?
//                    List<Category> categories = CategoryService.findById(inputParentCategoryId);
//                    for (Category category : categories) {
//                        System.out.println("ID: " + category.getId() + " Name: " + category.getName());
//                    }

                    break;
                case 4:
                    System.out.println("\n=== 상위 카테고리 수정 ===");
                    System.out.println("취소하려면 0을 입력하세요.");
                    System.out.print("수정할 상위 카테고리 id: ");

                    long updateCategoryId = scanner.nextInt();
                    scanner.nextLine();

                    if (updateCategoryId == 0) {
                        System.out.println("카테고리 수정이 취소되었습니다.");
                        break;
                    }

                    System.out.print("선택 카테고리의 변경할 이름: ");

                    String updateCategoryName = scanner.nextLine();

                    //TODO: 서비스 및 DTO 확인 필요
//                    CategoryRequestDTO dto = new CategoryRequestDTO(
//                            updateCategoryId,
//                            updateCategoryName,
//                            categoryLevel
//                    );

//                    CategoryService.updateCategory(dto);

                    break;
                case 5:
                    System.out.println("=== 카테고리 삭제 ===");
                    System.out.println("취소하려면 0을 입력하세요.");
                    System.out.print("삭제할 상위 카테고리 id: ");

                    long deleteCategoryId = scanner.nextInt();
                    scanner.nextLine();

                    if (deleteCategoryId == 0) {
                        System.out.println("카테고리 수정이 취소되었습니다.");
                        break;
                    }
                    //TODO: 서비스 확인필요
//                    CategoryService.deleteCategory(deleteCategoryId);
                    break;
                case 6:
                    System.out.println("=== 카테고리 선택 ===");
                    System.out.println("취소하려면 0을 입력하세요.");
                    System.out.print("선택 카테고리 id: ");

                    long inputCategoryId = scanner.nextInt();
                    scanner.nextLine();

                    if (inputCategoryId == 0) {
                        System.out.println("카테고리 등록이 취소되었습니다.");
                        break;
                    }
                    showCategoryMenu1(inputCategoryId);
                    break;
                case 7:
                    return;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택하세요");
                    break;
            }
        }
    }


    public void showCategoryMenu1(long parentId) {

        while (true) {
            System.out.println("\n=== 하위 컨텐츠 메뉴 ===");

            System.out.println("1. 하위 카테고리 목록");
            // id로 조회? , name으로 조회? (전체 조회)
            // level 1 이 아닌 카테고리 id 값이 들어오면?

            // level 0 카테고리에 등록
            System.out.println("2. 하위 카테고리 등록");

            // level 0 카테고리 조회
            System.out.println("3. 하위 카테고리 조회");
            // 조회 카테고리 내 course 목록 확인?
            // 이름으로 카테고리 조회?

            // 상위 카테고리 수정
            System.out.println("4. 하위 카테고리 수정");
            // level 0이 아닌 카테고리 id 값이 들어오면?

            System.out.println("5. 하위 카테고리 삭제");

            // 선택한 level1 카테고리 페이지 입장
            System.out.println("6. 하위 카테고리 선택");
            // 해당 카테고리 밑에 있는 강좌 메뉴 입장

            System.out.println("7. 뒤로가기");
            System.out.print("카테고리 메뉴 선택: ");

            int userInput = scanner.nextInt();
            scanner.nextLine();
        }
    }
}
