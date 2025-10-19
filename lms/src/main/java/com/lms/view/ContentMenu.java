package com.lms.view;

import java.util.Scanner;

public class ContentMenu {
    ContentManagement contentManagement = new ContentManagement();

    public void showContentMenu(Scanner scanner, int courseId) {

        System.out.print("섹션 ID 입력: ");
        int sectionId = scanner.nextInt();
        scanner.nextLine();

        while(true) {
            System.out.println("=== 콘텐츠 관리 메뉴 ===");
            System.out.println("1. 콘텐츠 추가");
            // System.out.println("2. 콘텐츠 수정");
            System.out.println("3. 콘텐츠 삭제");
            System.out.print("메뉴 선택: ");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1:
                    contentManagement.addContent(scanner, courseId, sectionId);
                    break;
                case 2:
                    //contentManagement.updateContent(scanner);
                    //break;
                case 3:
                    contentManagement.deleteContent(scanner, courseId, sectionId);
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }

    }
}
