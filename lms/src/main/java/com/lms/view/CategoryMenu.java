package com.lms.view;

import com.lms.domain.category.CategoryLevel;

import java.sql.SQLException;
import java.util.Scanner;

public class CategoryMenu {
    private final CategoryManagement categoryManagement;

    public CategoryMenu(CategoryManagement categoryManagement) {
        this.categoryManagement = categoryManagement;
    }

    public void showCategoryManagementMenu(Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("=== 관리자 메뉴 ===");
            System.out.println("1. 카테고리 추가");
            System.out.println("2. 카테고리 목록 보기");
            System.out.println("3. 카테고리 수정");
            System.out.println("4. 카테고리 삭제");
            System.out.println("5. 뒤로가기");
            System.out.print("카테고리 메뉴 선택: ");

            int managerInput = scanner.nextInt();
            scanner.nextLine();

            switch (managerInput) {
                case 1:
                    categoryManagement.addCategory(scanner);
                    break;
                case 2:
                    // 상위 카테고리 목록 조회
                    categoryManagement.showCategoryLevelOne(scanner);

                    // 상위 카테고리 id 입력받아 해당 카테고리의 하위 카테고리 목록 조회
                    categoryManagement.showCategoryLevelTwo(scanner);
                    break;
                case 3:
                    categoryManagement.updateCategory(scanner);
                    break;
                case 4:
                    categoryManagement.deleteCategory(scanner);
                case 5:
                    return;
            }
        }
    }
}