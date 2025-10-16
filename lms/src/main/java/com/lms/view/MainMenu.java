package com.lms.view;

import java.util.Scanner;

public class MainMenu {
    Scanner scanner = new Scanner(System.in);

    public void showMainMenu(){
        System.out.println("\nTeam Janban 1st_lms_project");
        while (true) {
            System.out.println("\n=== Main Menu === ");
            System.out.println("1. Category Menu"); // 이름, 부모 카테고리, 카테고리 레벨
            System.out.println("2. Course Menu");   // 이름, 소속 카테고리, 개요, detail, 작성자id, 생성 일시, 수정 일시
                                                    // 섹션 설정 (이름, 섹션 순서, 소속 강의id)
            System.out.println("3. Content Menu");  // 이름, 콘텐츠 순서, 소속 섹션id, 콘텐츠 개요(본문)
            System.out.println("4. Notice Menu");   // 소속 강의id, 공지사항 본문 파일 경로, 생성 일시, 수정 일시

            System.out.println("5. Exit");
            System.out.print("Select a menu option: ");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1:
                    new CategoryMenu().showCategoryMenu();
                    break;
                case 2:
                    new CourseMenu().showCourseMenu();
                    break;
                case 3:
                    new ContentMenu().showContentMenu();
                    break;
                case 4:
                    new NoticeMenu().showNoticeMenu();
                    break;
                case 5:
                    System.exit(0);
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택하세요");
            }
        }
    }
}
