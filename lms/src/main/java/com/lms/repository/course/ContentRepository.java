package com.lms.repository.course;

import com.lms.domain.course.Content;
import com.lms.domain.course.spec.rebuild.RebuildContent;
import com.lms.repository.exception.DatabaseException;

import java.util.List;
import java.util.Map;

public interface ContentRepository {

    /**
     * @param contentsBySectionId 섹션 id 별 컨텐츠 리스트
     * @return Section.id 별 List&lt;RebuildContent&gt;
     * @throws IllegalArgumentException <br/>
     * <ul>
     *     <li>contentsBySectionId 가 null</li>
     *     <li>contentsBySectionId.values 중 하나라도 null 이거나 empty</li>
     * </ul>
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    Map<Integer, List<RebuildContent>> createAllContentsOfSections(Map<Integer, List<Content>> contentsBySectionId);
}