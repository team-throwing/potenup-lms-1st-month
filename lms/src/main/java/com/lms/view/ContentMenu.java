package com.lms.view;

import java.util.Scanner;

public class ContentMenu {
    private final ContentManagement contentManagement = new ContentManagement();

    public void showContentMenu(Scanner scanner, int courseId) {

        System.out.print("섹션 ID 입력: ");
        int sectionId = scanner.nextInt();
        scanner.nextLine();

        while(true) {
            System.out.println("=== 콘텐츠 관리 메뉴 ===");
            System.out.println("1. 콘텐츠 추가");
            System.out.println("2. 콘텐츠 삭제");
            System.out.print("메뉴 선택: ");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1 -> contentManagement.addContent(scanner, courseId, sectionId);
                case 2 -> contentManagement.deleteContent(scanner, courseId, sectionId);
                default-> System.out.println("잘못된 입력입니다.");
            }
        }

    }
}
