package com.lms.repository.category;

import com.lms.domain.category.Category;
import com.lms.domain.category.CategoryLevel;
import com.lms.repository.exception.DatabaseException;

import java.util.List;

public interface CategoryRepository {

    /**
     * @param category 강좌 카테고리
     * @throws IllegalArgumentException <br/>
     *      <ul>
     *          <li>category 가 null</li>
     *          <li>category.id 가 not null</li>
     *      </ul>
     * @throws DatabaseException 복구 가능한 데이터베이스 예외입니다. 예를 들어, <br/>
     * <ul>
     *     <li>무결성 제약 조건 위반 (ex. 중복 키 삽입 시도, Not Null 위반, ...)</li>
     *     <li>잘못된 데이터 형식 (ex. 너무 긴 문자열, 오버 플로우, 언더 플로우, 잘못된 날짜 형식, ...)</li>
     *     <li>트랜잭션 실패 (ex. 데드락 감지, 락 대기시간 초과, ...)</li>
     *     <li>등등...</li>
     * </ul>
     */
    void create(Category category);

    /**
     * @param id 카테고리 id
     * @return 카테고리(not null)
     * @throws IllegalArgumentException id 가 음수
     * @throws DatabaseException 복구 가능한 데이터베이스 예외입니다. 예를 들어, <br/>
     * <ul>
     *     <li>무결성 제약 조건 위반 (ex. 중복 키 삽입 시도, Not Null 위반, ...)</li>
     *     <li>잘못된 데이터 형식 (ex. 너무 긴 문자열, 오버 플로우, 언더 플로우, 잘못된 날짜 형식, ...)</li>
     *     <li>트랜잭션 실패 (ex. 데드락 감지, 락 대기시간 초과, ...)</li>
     *     <li>등등...</li>
     * </ul>
     */
    Category findById(long id);

    /**
     * @param categoryLevel 카테고리 레벨
     * @return 해당 카테고리 레벨의 모든 카테고리 List. 이 List 는 비어있을 수 있음
     * @throws IllegalArgumentException categoryLevel 이 null
     * @throws DatabaseException 복구 가능한 데이터베이스 예외입니다. 예를 들어, <br/>
     * <ul>
     *     <li>무결성 제약 조건 위반 (ex. 중복 키 삽입 시도, Not Null 위반, ...)</li>
     *     <li>잘못된 데이터 형식 (ex. 너무 긴 문자열, 오버 플로우, 언더 플로우, 잘못된 날짜 형식, ...)</li>
     *     <li>트랜잭션 실패 (ex. 데드락 감지, 락 대기시간 초과, ...)</li>
     *     <li>등등...</li>
     * </ul>
     */
    List<Category> findAllByCategoryLevel(CategoryLevel categoryLevel);
    
    /**
     * @param id 하위 카테고리 id
     * @return 상위 카테고리 또는 null
     * @throws IllegalArgumentException id 가 음수
     * @throws DatabaseException 복구 가능한 데이터베이스 예외입니다. 예를 들어, <br/>
     * <ul>
     *     <li>무결성 제약 조건 위반 (ex. 중복 키 삽입 시도, Not Null 위반, ...)</li>
     *     <li>잘못된 데이터 형식 (ex. 너무 긴 문자열, 오버 플로우, 언더 플로우, 잘못된 날짜 형식, ...)</li>
     *     <li>트랜잭션 실패 (ex. 데드락 감지, 락 대기시간 초과, ...)</li>
     *     <li>등등...</li>
     * </ul>
     */
    Category findParentByChildId(long id);

    /**
     * @param id 상위 카테고리 id
     * @return 소속 하위 카테고리 List. 이 List 는 비어있을 수 있음
     * @throws DatabaseException 복구 가능한 데이터베이스 예외입니다. 예를 들어, <br/>
     * <ul>
     *     <li>무결성 제약 조건 위반 (ex. 중복 키 삽입 시도, Not Null 위반, ...)</li>
     *     <li>잘못된 데이터 형식 (ex. 너무 긴 문자열, 오버 플로우, 언더 플로우, 잘못된 날짜 형식, ...)</li>
     *     <li>트랜잭션 실패 (ex. 데드락 감지, 락 대기시간 초과, ...)</li>
     *     <li>등등...</li>
     * </ul>
     */
    List<Category> findChildrenByParentId(long id);

    /**
     * @return 모든 카테고리 List. 이 List 는 비어있을 수 있음.
     * @throws DatabaseException 복구 가능한 데이터베이스 예외입니다. 예를 들어, <br/>
     * <ul>
     *     <li>무결성 제약 조건 위반 (ex. 중복 키 삽입 시도, Not Null 위반, ...)</li>
     *     <li>잘못된 데이터 형식 (ex. 너무 긴 문자열, 오버 플로우, 언더 플로우, 잘못된 날짜 형식, ...)</li>
     *     <li>트랜잭션 실패 (ex. 데드락 감지, 락 대기시간 초과, ...)</li>
     *     <li>등등...</li>
     * </ul>
     */
    List<Category> findAll();
    
    /**
     * @param category 변경할 카테고리
     * @throws IllegalArgumentException <br/>
     *      <ul>
     *          <li>category 가 null</li>
     *          <li>category.id 가 null</li>
     *      </ul>
     * @throws DatabaseException 복구 가능한 데이터베이스 예외입니다. 예를 들어, <br/>
     * <ul>
     *     <li>무결성 제약 조건 위반 (ex. 중복 키 삽입 시도, Not Null 위반, ...)</li>
     *     <li>잘못된 데이터 형식 (ex. 너무 긴 문자열, 오버 플로우, 언더 플로우, 잘못된 날짜 형식, ...)</li>
     *     <li>트랜잭션 실패 (ex. 데드락 감지, 락 대기시간 초과, ...)</li>
     *     <li>등등...</li>
     * </ul>
     */
    void update(Category category);

    /**
     * @param id 삭제할 id
     * @throws IllegalArgumentException id 가 음수.
     * @throws DatabaseException 복구 가능한 데이터베이스 예외입니다. 예를 들어, <br/>
     * <ul>
     *     <li>무결성 제약 조건 위반 (ex. 중복 키 삽입 시도, Not Null 위반, ...)</li>
     *     <li>잘못된 데이터 형식 (ex. 너무 긴 문자열, 오버 플로우, 언더 플로우, 잘못된 날짜 형식, ...)</li>
     *     <li>트랜잭션 실패 (ex. 데드락 감지, 락 대기시간 초과, ...)</li>
     *     <li>등등...</li>
     * </ul>
     */
    void delete(long id);
}
