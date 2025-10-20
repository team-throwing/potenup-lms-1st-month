package com.lms.view;

import java.util.Scanner;

public class CategoryManu {
    private final CategoryManagement categoryManagement;

    public CategoryManu(CategoryManagement categoryManagement) {
        this.categoryManagement = categoryManagement;
    }

    public void showManagementMenu(Scanner scanner) {
        while (true) {
            System.out.println("=== 관리자 메뉴 ===");
            System.out.println("1. 카테고리 추가");
            System.out.println("2. 카테고리 목록 보기");
            System.out.println("3. 카테고리 수정");
            System.out.println("4. 카테고리 삭제");
            System.out.print("카테고리 메뉴 선택: ");

            int managerInput = scanner.nextInt();
            scanner.nextLine();

            switch (managerInput) {
                case 1:
                    categoryManagement.addCategory(scanner);
                    break;
                case 2:
                    categoryManagement.showCategory(scanner);
                    break;
                case 3:
                    categoryManagement.updateCategory(scanner);
                    break;
                case 4:
                    categoryManagement.deleteCategory(scanner);
            }
        }
    }
}