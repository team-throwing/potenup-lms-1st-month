package com.lms.view;

import com.lms.domain.category.Category;
import com.lms.domain.category.CategoryLevel;
import com.lms.dto.CategoryRequestDto;
import com.lms.service.CategoryService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

// 이름, 부모 카테고리, 카테고리 레벨
public class CategoryManagement {
    private final CategoryService categoryService;

    public CategoryManagement(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void addCategory(Scanner scanner) {
        System.out.println("=== 카테고리 추가 ===");
        System.out.print("카테고리 이름 입력: ");
        String addCategoryName = scanner.nextLine();
        System.out.print("카테고리 레벨 입력(1 or 2): ");
        int addCategoryLevel = scanner.nextInt();
        scanner.nextLine();

        CategoryLevel categoryLevel = null;
        Integer parentId = null;

        switch (addCategoryLevel) {
            case 1:
                categoryLevel = CategoryLevel.ONE;
                break;
            case 2:
                categoryLevel = CategoryLevel.TWO;
                System.out.print("상위 카테고리 ID 입력: ");
                parentId = scanner.nextInt();
                break;
            default:
                System.out.println("잘못된 입력입니다. 1 또는 2를 입력하세요.");
        }


        CategoryRequestDto dto = new CategoryRequestDto(addCategoryName, categoryLevel, parentId);
        categoryService.createCategory(dto);

    }

    public void showCategoryLevelOne(Scanner scanner) {
        System.out.println("=== 상위 카테고리 목록 ===");

//        List<Category> categories = categoryService.findCategoryByLevel(CategoryLevel.ONE);
//        for (Category category : categories) {
//            System.out.println("ID: " + category.getId() + "Title" + category.getName());
//        }
    }

    public void showCategoryLevelTwo(Scanner scanner) {
        System.out.println("=== 하위 카테고리 조회 ===");
        System.out.println("0 입력시 취소");
        System.out.print("상위 카테고리 선택(id): ");

        int inputId = scanner.nextInt();
        scanner.nextLine();

        if (inputId != 0) {
            System.out.println("=== 하위 카테고리 목록 ===");
//            List<Category> categories = categoryService.findCategoryByParentId(inputId);
//            for (Category category : categories) {
//            System.out.println("Id: " + category.getId() + "Title" + category.getName());
//        }
        } else {
            System.out.println("하위 카테고리 조회를 취소했습니다.");
        }

//
    }

    public void updateCategory(Scanner scanner) throws SQLException {
        System.out.println("=== 카테고리 수정 ===");
        System.out.print("수정할 카테고리 id 입력: ");
        Integer updateCategoryId = scanner.nextInt();
        scanner.nextLine();

        // 입력받은 카테고리 정보
        Category category = categoryService.findCategory(updateCategoryId.longValue());
        String categoryName = category.getName();
        CategoryLevel categoryLevel = category.getLevel();
        Integer updateParentId = category.getParentId();


        System.out.println("=공란 시 기존 값 유지=");
        System.out.print("\n카테고리 이름 변경: ");
        String updateCategoryName = scanner.nextLine();
        // 공란시 기존 값
        if (updateCategoryName.isBlank()){
            updateCategoryName = categoryName;
        }

        System.out.println("=공란 시 기존 값 유지=");
        System.out.print("\n카테고리 레벨 변경(1 or 2): ");
        String updateCategoryLevel = scanner.nextLine();
        scanner.nextLine();

        // 기본으로 기존 값 공란이 아닐 시 변경 값을 덮음
        if (!updateCategoryLevel.isBlank()){
            int levelNum = Integer.parseInt(updateCategoryLevel);

            switch (levelNum) {
                case 1:
                    categoryLevel = CategoryLevel.ONE;
                    updateParentId = null;
                    break;
                case 2:
                    categoryLevel = CategoryLevel.TWO;
                    System.out.println("=공란 시 기존 값 유지=");
                    System.out.print("소속 카테고리 ID 변경: ");
                    String inputParentId =  scanner.nextLine();
                    if (!inputParentId.isBlank()){
                        updateParentId = Integer.parseInt(inputParentId);
                    }
                    break;
                default:
                    System.out.println("잘못된 입력입니다. 기존 값을 유지합니다.");
                    return;
            }

        }
        CategoryRequestDto dto = new CategoryRequestDto(updateCategoryId, updateCategoryName, categoryLevel, updateParentId);
        categoryService.updateCategory(dto);
    }

    public void deleteCategory(Scanner scanner) {
        System.out.println("=== 카테고리 삭제 ===");
        System.out.println("삭제할 카테고리 id 입력: ");
        long deleteCategoryId = scanner.nextInt();
        scanner.nextLine();
        categoryService.deleteCategory(deleteCategoryId);
    }

}
