package com.lms.view;

import java.sql.SQLException;
import java.util.Scanner;

public class MainMenu {

    public void showMainMenu(Scanner scanner) throws SQLException {
        System.out.println("\nTeam Janban 1st_lms_project");
        while (true) {
            System.out.println("\n=== 메인 메뉴 ===");

            // 강좌 및 카테고리 조회 메뉴
            System.out.println("1. 강좌/카테고리 조회 메뉴");
            System.out.println("2. 관리자 메뉴");        // 카테고리 CRUD 메뉴
            System.out.println("3. 프로그램 종료");
            System.out.print("메뉴 선택: ");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1:
                    // 조회 메뉴 진입
                    new SearchMenu().showSearchMenu(scanner);
                    break;
                case 2:
                    // 카테고리 관리 메뉴 진입
                    new CategoryMenu().showCategoryManagementMenu(scanner);
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택하세요");
            }
        }
    }
}
