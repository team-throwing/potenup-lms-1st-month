package com.lms.view;

import java.util.Scanner;

// 이름, 부모 카테고리, 카테고리 레벨
public class CategoryMenu {
    private Scanner scanner = new Scanner(System.in);

    public void showCategoryMenu() {
        while (true) {
            System.out.println("\n=== Category Menu === ");
            System.out.println("1. Category 등록");
            System.out.println("2. Category 조회");
            System.out.println("3. Category 수정");
            System.out.println("4. Category 삭제");
            System.out.println("5. 뒤로가기");
            System.out.print("Select a Category option: ");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1:
                    System.out.println("Category 등록");

                    break;
                case 2:
                    System.out.println("Category 조회");

                    break;
                case 3:
                    System.out.println("Category 수정");

                    break;
                case 4:
                    System.out.println("Category 삭제");

                    break;
                case 5:
                    return;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택하세요");
            }
        }
    }
}
