package com.lms.view;

import com.lms.domain.course.spec.creation.CreateContent;
import com.lms.service.CourseService;

import java.util.NoSuchElementException;
import java.util.Scanner;

// 이름, 콘텐츠 순서, 소속 섹션id, 콘텐츠 개요(본문)
public class ContentManagement {
    private final CourseService courseService  = new CourseService();

    public void addContent(Scanner scanner, int courseId, int sectionId) {
        System.out.println("=== 섹션에 콘텐츠 추가 ===");

        try {
            System.out.print("콘텐츠 이름 입력: ");
            String name = scanner.nextLine();

            System.out.print("콘텐츠 순서(seq) 입력: ");
            int seq = Integer.parseInt(scanner.nextLine());

            System.out.print("콘텐츠 내용 입력: ");
            String body = scanner.nextLine();

            // CreateContent 객체 생성
            CreateContent contentSpec = new CreateContent(name, seq, body);

            // 서비스 호출
            courseService.addContent(courseId, sectionId, contentSpec);

            System.out.println("콘텐츠가 섹션에 성공적으로 추가되었습니다.");
        } catch (NumberFormatException e) {
            System.out.println("잘못된 숫자 입력입니다. 다시 시도해주세요.");
        } catch (NoSuchElementException e) {
            System.out.println("해당 코스 또는 섹션을 찾을 수 없습니다: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("콘텐츠 추가 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public void updateContent(Scanner scanner, int courseId, int sectionId) {}

    public void deleteContent(Scanner scanner, Integer courseId, Integer sectionId) {
        System.out.println("=== 섹션 내 콘텐츠 삭제 ===");

        try {
            System.out.print("삭제할 콘텐츠 ID 입력: ");
            long contentId = Long.parseLong(scanner.nextLine());

            // 서비스 호출
            courseService.deleteContent(courseId, sectionId, contentId);

            System.out.println("콘텐츠가 성공적으로 삭제되었습니다.");
        } catch (NumberFormatException e) {
            System.out.println("잘못된 숫자 입력입니다. 다시 시도해주세요.");
        } catch (NoSuchElementException e) {
            System.out.println("해당 코스 또는 섹션을 찾을 수 없습니다: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("콘텐츠 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }


}
