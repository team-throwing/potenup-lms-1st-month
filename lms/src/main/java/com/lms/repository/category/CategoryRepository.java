package com.lms.repository.category;

import com.lms.domain.category.Category;
import com.lms.domain.category.CategoryLevel;
import com.lms.repository.exception.ConstraintViolationException;
import com.lms.repository.exception.RecordNotFoundException;
import com.lms.repository.exception.OrphanNotAllowedException;

import java.util.List;

public interface CategoryRepository {

    /**
     * @param category 강좌 카테고리
     * @throws IllegalArgumentException <br/>
     *      <ul>
     *          <li>category 가 null</li>
     *          <li>category.id 가 not null</li>
     *      </ul>
     * @throws ConstraintViolationException <br/>
     *      <ul>
     *          <li>name 의 길이(255 바이트) 초과</li>
     *          <li>dbms 에서 허용하고 있지 않은 카테고리 level</li>
     *          <li>명시된 parentId 에 해당하는 category 가 존재하지 않음</li>
     *      </ul>
     */
    void create(Category category);

    /**
     * @param id 카테고리 id
     * @throws IllegalArgumentException id 가 음수
     * @throws RecordNotFoundException id 에 해당하는 카테고리가 없음
     * @return 카테고리(not null)
     */
    Category findById(long id);

    /**
     * @param categoryLevel 카테고리 레벨
     * @throws IllegalArgumentException categoryLevel 이 null
     * @return 해당 카테고리 레벨의 모든 카테고리 List. 이 List 는 비어있을 수 있음
     */
    List<Category> findAllByCategoryLevel(CategoryLevel categoryLevel);
    
    /**
     * @param id 하위 카테고리 id
     * @throws IllegalArgumentException id 가 음수
     * @throws RecordNotFoundException id 에 해당하는 카테고리를 찾을 수 없음
     * @return 상위 카테고리 또는 null
     */
    Category findParentByChildId(long id);

    /**
     * @param id 상위 카테고리 id
     * @throws RecordNotFoundException id 에 해당하는 카테고리를 찾을 수 없음
     * @return 소속 하위 카테고리 List. 이 List 는 비어있을 수 있음
     */
    List<Category> findChildrenByParentId(long id);

    /**
     * @return 모든 카테고리 List. 이 List 는 비어있을 수 있음.
     */
    List<Category> findAll();
    
    /**
     * @param category 변경할 카테고리
     * @throws IllegalArgumentException <br/>
     *      <ul>
     *          <li>category 가 null</li>
     *          <li>category.id 가 null</li>
     *      </ul>
     * @throws RecordNotFoundException category.id 에 해당하는 카테고리를 찾을 수 없음.
     * @throws ConstraintViolationException <br/>
     *      <ul>
     *          <li>name 의 길이(255 바이트) 초과</li>
     *          <li>dbms 에서 허용하고 있지 않은 카테고리 level</li>
     *          <li>명시된 parentId 에 해당하는 category 가 존재하지 않음</li>
     *      </ul>
     */
    void update(Category category);

    /**
     * @param id 삭제할 id
     * @throws IllegalArgumentException id 가 음수.
     * @throws RecordNotFoundException id 에 해당하는 카테고리를 찾을 수 없음.
     * @throws OrphanNotAllowedException DBMS 에 DELETE 연산에 대한 고아 카테고리가 허용되지 않음
     */
    void delete(long id);
}
