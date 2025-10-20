package com.lms.view;

import java.sql.SQLException;
import java.util.Scanner;

public class NoticeMenu {
    private final NoticeManagement noticeManagement = new NoticeManagement();

    public void showNoticeMenu(Scanner scanner, int courseId) throws SQLException {
        while (true) {
            System.out.println("=== 공지 메뉴 ===");
            System.out.println("1. 공지 생성");
            System.out.println("2. 공지 수정");
            System.out.println("3. 공지 삭제");
            System.out.println("4. 뒤로가기");
            System.out.print("메뉴 입력: ");

            int userInput = scanner.nextInt();
            scanner.nextLine();

            switch (userInput) {
                case 1 -> noticeManagement.addNotice(scanner, courseId);
                case 2 -> noticeManagement.updateNotice(scanner);
                case 3 -> noticeManagement.deleteNotice(scanner);
                case 4 -> {
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }
}
