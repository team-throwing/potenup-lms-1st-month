package com.lms.view;

// 이름, 소속 카테고리, 개요, detail, 작성자id, 생성 일시, 수정 일시
// 섹션 설정 (이름, 섹션 순서, 소속 강의id)
public class CourseManagement {

    public void showCourseMenu(int searchCourseId) {
        while (true) {
            System.out.println("\n=== 코스 메뉴 === ");
            System.out.println("1. 코스 보기");
            System.out.println("2. 코스 수정");

            System.out.println("3. 코스 수정");
                // Course_id로 선택하여 수정
                // Course(title, category_id, summary, detail, user_id, updated_at) 수정 > user_id 수정 가능?
                // Section(name, seq, course_id) 수정
                // content(name, seq, section_id, body) 수정

            System.out.println("4. 코스 삭제");
            System.out.println("5. 섹션 관리");
            System.out.println("6. 메인 메뉴로");
            System.out.print("코스 메뉴 선택: ");

//            int userInput = scanner.nextInt();
//            scanner.nextLine();

//            switch (userInput) {
//                case 1:
//                    System.out.println("Course 등록");
//
//                    break;
//                case 2:
//                    System.out.println("Course 조회");
//
//                    break;
//                case 3:
//                    System.out.println("Course 수정");
//
//                    break;
//                case 4:
//                    System.out.println("Course 삭제");
//
//                    break;
//                case 5:
//                    return;
//                default:
//                    System.out.println("잘못된 선택입니다. 다시 선택하세요");
//            }
        }
    }
    public void searchCourseByKeyword() {

    }

    public void createCourse(int categoryId) {
        while (true) {
            System.out.println("=== 코스 생성 메뉴 ===");
            System.out.println("취소하려면 0을 입력하세요.");
            System.out.print("생성할 코스의 이름: ");
//            String courseName = scanner.nextLine();

            System.out.print("\n생성할 코스의 개요(공란 가능): ");
//            String courseSummary = scanner.nextLine();

//            if (courseSummary.equals("0")) {
//                break;
//            } else if (courseSummary.trim().isEmpty()) {
//                courseSummary = null;
//            }

            System.out.print("\n 생성할 코스의 상세 설명(공란 가능): ");
//            String courseDetail = scanner.nextLine();

//            if (courseDetail.trim().isEmpty()) {
//                courseDetail = null;
//            }

            System.out.print("코스를 등록하는 유저: ");
//            long createUserId = scanner.nextLong();
//            scanner.nextLine();

//            CourseRequestDto dto = new CourseDto(courseName, courseSummary, courseDetail, createUserId);
//            new CourseService.createCourse(dto);
        }
    }
}
