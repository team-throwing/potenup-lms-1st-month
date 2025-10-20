package com.lms.view;

import com.lms.service.NoticeService;

import java.util.Scanner;

public class NoticeManagement {
    private final NoticeService noticeService = new NoticeService();
    public void addNotice(Scanner scanner, int courseId) {
        System.out.println("=== 공지 생성 ===");
        System.out.print("공지 내용 입력: ");
        String noticeBody = scanner.nextLine();
        noticeService.createNotice(noticeBody, courseId);
    }

    public void updateNotice(Scanner scanner) {
        System.out.println("=== 공지 수정 ===");
        System.out.print("수정할 공지 id 입력: ");
        int userInput = scanner.nextInt();
        scanner.nextLine();

        System.out.print("공지 수정 내용: ");
        String newBodyInput = scanner.nextLine();

        noticeService.updateNotice((long) userInput, newBodyInput);
    }

    public void deleteNotice(Scanner scanner) {
        System.out.println("=== 공지 삭제 ===");
        System.out.print("삭제할 공지 id 입력: ");
        int userInput = scanner.nextInt();
        scanner.nextLine();

        noticeService.deleteNotice((long) userInput);
    }
}
