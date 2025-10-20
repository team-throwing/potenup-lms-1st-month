package com.lms.view;

import com.lms.domain.course.spec.creation.CreateContent;
import com.lms.domain.course.spec.creation.CreateSection;
import com.lms.service.CourseService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SectionManagement {
    CourseService courseService = new CourseService();

    public void addSection(Scanner scanner, int courseId) {
        System.out.println("=== 섹션 추가 ===");

        try {

            // 섹션 순서(seq) 입력
            System.out.print("섹션 순서(seq) 입력 (0부터 시작): ");
            int seq = Integer.parseInt(scanner.nextLine());

            // 섹션 이름 입력
            System.out.print("섹션 이름 입력: ");
            String sectionName = scanner.nextLine();

            // 콘텐츠 개수 입력
            System.out.print("추가할 콘텐츠 개수 입력: ");
            int contentCount = Integer.parseInt(scanner.nextLine());

            List<CreateContent> contentList = new ArrayList<>();

            for (int i = 0; i < contentCount; i++) {
                System.out.println("\n[콘텐츠 " + (i + 1) + "]");

                System.out.print("콘텐츠 이름 입력: ");
                String contentName = scanner.nextLine();

                System.out.print("콘텐츠 순서(seq) 입력: ");
                int contentSeq = Integer.parseInt(scanner.nextLine());

                System.out.print("콘텐츠 본문 입력: ");
                String contentBody = scanner.nextLine();

                CreateContent content = new CreateContent(contentName, contentSeq, contentBody);
                contentList.add(content);
            }

            // 섹션 생성
            CreateSection createSection = new CreateSection(seq, sectionName, contentList);

            // 서비스 호출
            courseService.addSection(courseId, createSection);

            System.out.println("섹션이 성공적으로 추가되었습니다.");

        } catch (NumberFormatException e) {
            System.out.println("숫자를 잘못 입력했습니다. 다시 시도해주세요.");
        } catch (Exception e) {
            System.out.println("섹션 추가 중 오류 발생: " + e.getMessage());
        }
    }

    public void deleteSection(Scanner scanner, int courseId) {
        System.out.println("=== 섹션 삭제 ===");

        try {
            // 삭제할 섹션 ID 입력
            System.out.print("삭제할 섹션 ID 입력: ");
            int sectionId = Integer.parseInt(scanner.nextLine());

            // 서비스 호출
            courseService.deleteSection(courseId, sectionId);

            System.out.println("섹션이 성공적으로 삭제되었습니다.");
        } catch (NumberFormatException e) {
            System.out.println("숫자를 잘못 입력했습니다. 다시 시도해주세요.");
        } catch (NoSuchElementException e) {
            System.out.println("해당 코스나 섹션이 존재하지 않습니다: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("섹션 삭제 중 오류 발생: " + e.getMessage());
        }
    }


}
