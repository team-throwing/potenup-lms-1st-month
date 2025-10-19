package com.lms.view;

import java.util.Scanner;

public class SectionMenu {
    SectionManageMent sectionManageMent = new SectionManageMent();
    ContentMenu contentMenu = new ContentMenu();

    public void showSectionMenu(Scanner scanner, int courseId) {
        while (true) {
            System.out.println("=== 섹션 관리 메뉴 ===");
            System.out.println("1. 섹션 추가");
            //System.out.println("2. 섹션 수정");
            System.out.println("3. 섹션 삭제");
            System.out.println("4. 콘텐츠 관리 메뉴");
            System.out.println("5. 뒤로가기");
            System.out.print("메뉴 선택: ");
            int sectionSelected = scanner.nextInt();
            scanner.nextLine();

            switch (sectionSelected) {
                case 1:
                    sectionManageMent.addSection(scanner, courseId);
                    break;
                case 2:
                    //sectionManageMent.updateSection();
                    break;
                case 3:
                    sectionManageMent.deleteSection(scanner, courseId);
                    break;
                case 4:
                    contentMenu.showContentMenu(scanner, courseId);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }
}
