package com.lms.view;

import java.util.Scanner;

// 이름, 콘텐츠 순서, 소속 섹션id, 콘텐츠 개요(본문)
public class ContentMenu {
    Scanner scanner = new Scanner(System.in);

    public void showContentMenu() {

        while (true) {
            System.out.println("\n=== Content Menu === ");
            System.out.println("1. Content 등록");
            System.out.println("2. Content 조회");
            System.out.println("3. Content 수정");
            System.out.println("4. Content 삭제");
            System.out.println("5. 뒤로가기");
            System.out.print("Select a Content option: ");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1:
                    System.out.println("Content 등록");

                    break;
                case 2:
                    System.out.println("Content 조회");

                    break;
                case 3:
                    System.out.println("Content 수정");

                    break;
                case 4:
                    System.out.println("Content 삭제");

                    break;
                case 5:
                    return;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택하세요");
            }
        }
    }
}
