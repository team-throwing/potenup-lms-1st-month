package com.lms.repository.category;

import com.lms.domain.category.Category;
import com.lms.domain.category.CategoryLevel;
import com.lms.repository.exception.DatabaseException;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    /**
     * @param category 강좌 카테고리
     * @return 카테고리(null 일 수 있으므로 반드시 확인 해야 함)
     * @throws IllegalArgumentException <br/>
     *      <ul>
     *          <li>category 가 null</li>
     *          <li>category.id 가 not null</li>
     *      </ul>
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    Category create(Category category);

    /**
     * @param id 카테고리 id
     * @return 카테고리 또는 Optional.empty()
     * @throws IllegalArgumentException id 가 음수
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    Optional<Category> findById(int id);

    /**
     * @param categoryLevel 카테고리 레벨
     * @return 해당 카테고리 레벨의 모든 카테고리 List. 이 List 는 비어있을 수 있음
     * @throws IllegalArgumentException categoryLevel 이 null
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    List<Category> findAllByCategoryLevel(CategoryLevel categoryLevel);

    /**
     * @param id 상위 카테고리 id
     * @return 소속 하위 카테고리 List. 이 List 는 비어있을 수 있음
     * @throws IllegalArgumentException id 가 음수
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    List<Category> findChildrenByParentId(int id);

    /**
     * @return 모든 카테고리 List. 이 List 는 비어있을 수 있음.
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    List<Category> findAll();
    
    /**
     * @param category 변경할 카테고리
     * @throws IllegalArgumentException <br/>
     *      <ul>
     *          <li>category 가 null</li>
     *          <li>category.id 가 null</li>
     *      </ul>
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    void update(Category category);

    /**
     * @param id 삭제할 id
     * @throws IllegalArgumentException id 가 음수.
     * @throws DatabaseException 복구 가능한 데이터베이스 예외
     */
    void delete(int id);
}
