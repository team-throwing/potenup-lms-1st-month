package com.lms.view;

import java.util.Scanner;

// 소속 강의id, 공지사항 본문 파일 경로, 생성 일시, 수정 일시
public class NoticeMenu {
    Scanner scanner = new Scanner(System.in);

    public void showNoticeMenu() {

        while (true) {
            System.out.println("\n=== Notice Menu === ");
            System.out.println("1. Notice 등록");
            System.out.println("2. Notice 조회");
            System.out.println("3. Notice 수정");
            System.out.println("4. Notice 삭제");
            System.out.println("5. 뒤로가기");
            System.out.print("Select a Notice option: ");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1:
                    System.out.println("Notice 등록");

                    break;
                case 2:
                    System.out.println("Notice 조회");

                    break;
                case 3:
                    System.out.println("Notice 수정");

                    break;
                case 4:
                    System.out.println("Notice 삭제");

                    break;
                case 5:
                    return;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택하세요");
            }
        }
    }
}
