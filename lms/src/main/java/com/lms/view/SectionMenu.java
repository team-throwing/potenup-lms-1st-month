package com.lms.view;

import java.util.Scanner;

public class SectionMenu {
    private final SectionManageMent sectionManageMent = new SectionManageMent();
    private final ContentMenu contentMenu = new ContentMenu();

    public void showSectionMenu(Scanner scanner, int courseId) {
        while (true) {
            System.out.println("=== 섹션 관리 메뉴 ===");
            System.out.println("1. 섹션 추가");
            System.out.println("2. 섹션 삭제");
            System.out.println("3. 콘텐츠 관리 메뉴");
            System.out.println("4. 뒤로가기");
            System.out.print("메뉴 선택: ");
            int sectionSelected = scanner.nextInt();
            scanner.nextLine();

            switch (sectionSelected) {
                case 1 -> sectionManageMent.addSection(scanner, courseId);
                case 2 -> sectionManageMent.deleteSection(scanner, courseId);
                case 3 -> contentMenu.showContentMenu(scanner, courseId);
                case 4 -> {
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }
}
