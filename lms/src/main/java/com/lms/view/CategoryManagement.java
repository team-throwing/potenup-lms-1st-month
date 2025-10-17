package com.lms.view;

import com.lms.domain.category.CategoryLevel;
import com.lms.dto.CategoryRequestDto;
import com.lms.service.CategoryService;

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

        if (categoryLevel != null) {
            CategoryRequestDto dto = new CategoryRequestDto(addCategoryName, categoryLevel, parentId);
            categoryService.createCategory(dto);
        }
    }
    public void showCategory(Scanner scanner) {
        System.out.println("=== 카테고리 목록 조회 메뉴 ===");
        System.out.println("1. 카테고리 레벨에 속한 목록 조회");
        System.out.println("2. 부모 카테고리에 속한 하위 카테고리 목록 조회");
        System.out.print("조회 메뉴 선택: ");
        int categorySearchInput = scanner.nextInt();
        scanner.nextLine();

        switch (categorySearchInput) {
            case 1:
                // 서비스 없음
            case 2:
                // 서비스 없음
        }
    }

    public void updateCategory(Scanner scanner) {
        System.out.println("=== 카테고리 수정 ===");
        System.out.print("수정할 카테고리 id 입력: ");
        Integer updateCategoryId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("\n카테고리 이름 변경: ");
        String updateCategoryName = scanner.nextLine();
        System.out.print("\n카테고리 레벨 변경(1 or 2): ");
        int updateCategoryLevel = scanner.nextInt();
        scanner.nextLine();

        CategoryLevel categoryLevel = null;
        Integer updateParentId = null;

        switch (updateCategoryLevel) {
            case 1:
                categoryLevel = CategoryLevel.ONE;
                break;
            case 2:
                categoryLevel = CategoryLevel.TWO;
                System.out.print("\n소속 카테고리 id 변경: ");
                updateParentId = scanner.nextInt();
                break;
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
