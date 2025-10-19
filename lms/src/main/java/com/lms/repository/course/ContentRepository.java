package com.lms.repository.course;

import com.lms.domain.course.Content;
import com.lms.domain.course.Course;
import com.lms.domain.course.spec.rebuild.RebuildContent;
import com.lms.repository.exception.DatabaseException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ContentRepository {

    /**
     * @param contentsBySectionId 섹션 id 별 컨텐츠 리스트
     * @return Section.id 별 List&lt;RebuildContent&gt;
     * @throws IllegalArgumentException contentsBySectionId 가 null
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    Map<Integer, List<RebuildContent>> createAllContentsOfSections(Map<Integer, List<Content>> contentsBySectionId);

    /**
     * @param contentIds 제거할 컨텐츠 id 집합
     * @throws IllegalArgumentException contentIds 가 null
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    void deleteAll(Set<Long> contentIds);
    
    /**
     * 각 컨텐츠에 대해서 컨텐츠 id 가<br/>
     * null 인 경우에는 생성하고 <br/>
     * null 이 아닌 경우에는 수정한다.
     * @param contentsBySectionId 섹션 id 별 컨텐츠 리스트
     * @throws IllegalArgumentException contentsBySectionId 또는 originalCourse 가 null
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    void updateAllContentsOfSections(Map<Integer, List<Content>> contentsBySectionId, Course originalCourse);
}