package com.lms.domain.view;

import java.util.Scanner;

// 이름, 소속 카테고리, 개요, detail, 작성자id, 생성 일시, 수정 일시
// 섹션 설정 (이름, 섹션 순서, 소속 강의id)
public class CourseMenu {
    Scanner scanner = new Scanner(System.in);

    public void showCourseMenu() {
        while (true) {
            System.out.println("\n=== Course Menu === ");
            System.out.println("1. Course 등록");
            System.out.println("2. Course 조회");
            System.out.println("3. Course 수정");
            System.out.println("4. Course 삭제");
            System.out.println("5. 뒤로가기");
            System.out.print("Select a Course option: ");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1:
                    System.out.println("Course 등록");

                    break;
                case 2:
                    System.out.println("Course 조회");

                    break;
                case 3:
                    System.out.println("Course 수정");

                    break;
                case 4:
                    System.out.println("Course 삭제");

                    break;
                case 5:
                    return;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택하세요");
            }
        }
    }
}
