package com.lms.repository.course.dto;

import com.lms.domain.category.Category;
import com.lms.repository.course.CourseRepository;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * 별도의 검색 조건이 없다면 비워둔 채로 생성 가능 함. 예를 들어,
 *
 * <br/>
 * <pre>
 *
 *     CourseSearchFilter filter
 *          = CourseSearchFilter.builder().build()</pre>
 * <br/>
 *
 * 이 경우의 검색 결과는 {@link CourseRepository} 의 search 메서드를 참조
 *
 * @param keyword 키워드<br/>
 *        → null 인 경우 키워드 검색 적용 x
 * @param keywordSearchScope 키워드 검색 범위(ex. 제목, 제목|개요, 제목|개요|본문)<br/>
 *        → keyword 가 null 이 아닌 경우에만 고려 됨
 *        → keyword 가 null 이 아니고, 이 컬럼이 null 인 경우 기본값 KeyWordSearchScope.TITLE 로 설정 됨<br/>
 * @param categorySearchScope 카테고리 검색 범위
 *        → null 또는 empty 인 경우 전체 카테고리에서 탐색
 * @param createdAtFrom 생성 일자 여기 부터
 *        → null 인 경우 무제한
 * @param createdAtTo 여기까지
 *        → null 인 경우 무제한
 * @param updatedAtFrom 수정 일자 여기 부터
 *        → null 인 경우 무제한
 * @param updatedAtTo 여기까지
 *        → null 인 경우 무제한
 * @param userName 강사 이름
 *        → null 인 경우 keyword 가 강사 이름으로도 검색됨
 *        → keyword 도 null 인 경우 전체 강사에서 검색
 * @param pageSize 페이지 크기
 *        → null 인 경우 기본값 = 10
 * @param pageNum 페이지 번호
 *        → null 인 경우 기본값 = 1
 */
@Builder
public record CourseInfoSearchFilter(
        String keyword,
        KeywordSearchScope keywordSearchScope,
        List<Category> categorySearchScope,
        LocalDate createdAtFrom,
        LocalDate createdAtTo,
        LocalDate updatedAtFrom,
        LocalDate updatedAtTo,
        String userName,
        Integer pageSize,
        Integer pageNum
) {
    public CourseInfoSearchFilter {
        // To Do: keyword 길이 제한 및 문자 제한(공격 방지)

        // To Do: keywordSearchScope 가 null 인 경우 기본값으로 초기화

        // To Do: categorySearchScope 내 id 중복 제거 및 불필요한 id 제거
        /*
            [불필요한 id 검출]
            for each `category` in `list of categories`:
                if `category` is subcategory and `category's parent` is in `the list`:
                    remove `category` in `the list`
         */

        // To Do: createdAtFrom 과 createdAtTo 의 순서 검증

        // To Do: updatedAtFrom 과 updatedAtTo 의 순서 검증

        // To Do: userName 길이 제한 및 문자 제한(공격 방지)

        // To Do: pageSize 값 범위 검증: 1 이상 100 이하
        // To Do: pageSize 가 null 인 경우 기본값으로 초기화
        if (pageSize == null) {
            pageSize = 10;
        }
        // To Do: pageNum 값 범위 검증: 1 이상
        // To Do: pageNum 이 null 인 경우 기본값으로 초기화
        if (pageNum == null) {
            pageNum = 1;
        }
    }
}