package com.lms.view;

import com.lms.domain.category.CategoryLevel;

import java.sql.SQLException;
import java.util.Scanner;

public class CategoryMenu {
    private final CategoryManagement categoryManagement = new CategoryManagement();

    public void showCategoryManagementMenu(Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("=== 관리자 메뉴 ===");
            System.out.println("1. 카테고리 추가");
            System.out.println("2. 카테고리 목록 보기");
            System.out.println("3. 카테고리 수정");
            System.out.println("4. 카테고리 삭제");
            System.out.println("5. 뒤로가기");
            System.out.print("카테고리 메뉴 선택: ");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1 -> categoryManagement.addCategory(scanner);
                case 2 -> categoryManagement.findAllCategory(scanner);
                case 3 -> categoryManagement.updateCategory(scanner);
                case 4 -> categoryManagement.deleteCategory(scanner);
                case 5 -> {return;}
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }
}