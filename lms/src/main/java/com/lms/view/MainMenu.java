package com.lms.view;

import java.util.Scanner;

public class MainMenu {
    Scanner scanner = new Scanner(System.in);

    public void showMainMenu(){
        System.out.println("\nTeam Janban 1st_lms_project");
        while (true) {
            System.out.println("\n=== 메인 메뉴 ===");

            // 이름, 부모 카테고리, 카테고리 레벨
            System.out.println("1. 카테고리 메뉴");
            // 카테고리 level 0 목록 출력 + 카테고리 메뉴 진입

            // Course: 이름, 소속 카테고리, 개요, detail, 작성자id, 생성 일시, 수정 일시
            // Section: 섹션 설정 (이름, 섹션 순서, 소속 강의id)
            // Content: 이름, 콘텐츠 순서, 소속 섹션id, 콘텐츠 개요(본문)
            System.out.println("2. 강좌 메뉴");

            System.out.println("3. 프로그램 종료");
            System.out.print("메뉴 선택: ");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1:
                    new CategoryMenu().showCategoryMenu0();

                    break;
                case 2:
                    new CourseMenu().showCourseMenu();
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택하세요");
            }
        }
    }
}
