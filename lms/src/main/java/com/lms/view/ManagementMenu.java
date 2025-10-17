package com.lms.view;

import com.lms.domain.category.Category;
import com.lms.domain.category.CategoryLevel;

import java.util.List;
import java.util.Scanner;

public class ManagementMenu {

    public void showManagementMenu() {
        Scanner scanner = new Scanner(System.in);
        CategoryService categoryService = new CategoryService();

        CategoryLevel categoryLevel;
        while (true) {
            System.out.println("=== 카테고리 관리 메뉴 ===");
            System.out.println("1. 카테고리 추가");
            System.out.println("2. 카테고리 목록 보기");
            System.out.println("3. 카테고리 수정");
            System.out.println("4. 카테고리 삭제");
            System.out.print("카테고리 메뉴 선택: ");

            int managerInput = scanner.nextInt();
            scanner.nextLine();

            switch (managerInput) {
                case 1:
//                    new CategoryMenu().addCategory();
//                    break;
                case 2:
                    System.out.print("=== 상위 카테고리 목록 ===");
//                    List<Category> categories = categoryService.findAllByCategoryLevel(CategoryLevel.ONE);
//                    for (Category category : categories) {
//                        System.out.println("ID: " + category.getId() + "Name" + category.getName());
//                    }

                    System.out.println("카테고리 선택(id): ");
                    int inputCategoryId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("=== 하위 카테고리 목록 ===");
//                    List<Category> categories2 = categoryService.findChildrenByParentId(inputCategoryId);
//                    for (Category category : categories) {
//                        System.out.println("ID: " + category.getId() + "Name" + category.getName());
//                    }
                case 3:
                    System.out.println("=== 카테고리 수정 ===");
                    System.out.print("수정할 카테고리 id 입력: ");
                    int updateCategoryId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("\n수정된 카테고리 이름: ");
                    String updateCategoryName = scanner.nextLine();

                    System.out.print("수정된 카테고리 레벨(1, 2): ");
                    int updateCategoryLevel = scanner.nextInt();
                    scanner.nextLine();

                    if (updateCategoryLevel == 1) {
                        categoryLevel = CategoryLevel.ONE;
                    } else if (updateCategoryLevel == 2) {
                        categoryLevel = CategoryLevel.TWO;
                    } else {
                        break;
                    }

                    System.out.println("수정된 카테고리의 부모 카테고리 id(없으면 공란): ");
                    String input = scanner.nextLine().trim();

                    Integer updateCategoryParentId = null;

                    if (!input.isEmpty()) {
                        try {
                            updateCategoryParentId = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            System.out.println("숫자가 아닌 값이 입력되었습니다.");
                        }
                    }

                    CategoryRequestDto dto = new CategoryRequestDto(
                            updateCategoryParentId,
                            updateCategoryName,
                            categoryLevel,
                            updateCategoryParentId
                            );
                    categoryService.updateCategory(dto);
                case 4:
                    System.out.println("=== 카테고리 삭제 ===");
                    System.out.println("취소하려면 0을 입력하세요.");
                    System.out.println("삭제할 코스 id 입력: ");

                    int deleteCourseId = scanner.nextInt();
                    scanner.nextLine();

                    if (deleteCourseId == 0) {
                        System.out.println("코스 선택 취소");
                        break;
                    }

                    categoryService.deleteCategory(deleteCourseId);
            }
        }
    }
}
