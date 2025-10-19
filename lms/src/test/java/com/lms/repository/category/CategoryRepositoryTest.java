package com.lms.repository.category;

import com.lms.domain.category.Category;
import com.lms.domain.category.CategoryLevel;
import com.lms.repository.config.DataSourceFactory;
import com.lms.repository.config.RepositoryConfig;
import com.lms.service.ConnectionHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest {

    Connection conn;

    private final CategoryRepository categoryRepository
            = RepositoryConfig.categoryRepository();

    @BeforeEach
    void startTransaction() throws SQLException {
        conn = DataSourceFactory.get().getConnection();
        ConnectionHolder.set(conn);
        conn.setAutoCommit(false);
    }

    @AfterEach
    void closeConnection() throws SQLException {
        conn.close();
    }

    void rollback(SQLException e, Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        System.err.println("==================");
        e.printStackTrace();
    }

    @Test
    void create() {
        try {

            // 새 상위 카테고리 생성
            Category newCategory = Category.create(
                    "새 상위 카테고리",
                    CategoryLevel.ONE,
                    null
            );
            Category created = categoryRepository.create(newCategory);

            // 새 하위 카테고리 생성
            Category newSubCategory = Category.create(
                    "새 하위 카테고리",
                    CategoryLevel.TWO,
                    created.getId()
            );
            Category createdSub = categoryRepository.create(newSubCategory);

            // asserts
            assertEquals(newCategory.getName(), created.getName());
            assertEquals(newCategory.getLevel(), created.getLevel());
            assertEquals(newCategory.getParentId(), created.getParentId());

            assertEquals(newSubCategory.getName(), createdSub.getName());
            assertEquals(newSubCategory.getLevel(), createdSub.getLevel());
            assertEquals(newSubCategory.getParentId(), createdSub.getParentId());

            conn.commit();
        } catch (SQLException e) {
            rollback(e, conn);
        }
    }

    @Test
    void findById() {
        try {

            // do test
            Optional<Category> optionalCategory
                    = categoryRepository.findById(1);

            assertTrue(optionalCategory.isPresent());

            conn.commit();
        } catch (SQLException e) {
            rollback(e, conn);
        }
    }

    @Test
    void findAllByCategoryLevel() {
        try {

            // do test
            List<Category> superCategories
                    = categoryRepository.findAllByCategoryLevel(CategoryLevel.ONE);
            List<Category> subCategories
                    = categoryRepository.findAllByCategoryLevel(CategoryLevel.TWO);

            for (Category superCategory : superCategories) {
                assertEquals(CategoryLevel.ONE, superCategory.getLevel());
            }

            for (Category subCategory : subCategories) {
                assertEquals(CategoryLevel.TWO, subCategory.getLevel());
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(e, conn);
        }
    }

    @Test
    void findChildrenByParentId() {
        try {

            // do test
            List<Category> children = categoryRepository.findChildrenByParentId(1);

            for (Category child : children) {
                assertEquals(1, child.getParentId());
                printCategory(child);
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(e, conn);
        }
    }

    @Test
    void findAll() {
        try {

            // do test
            List<Category> allCategories
                    = categoryRepository.findAll();

            conn.commit();
        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    void update() {
        try {

            // do test
            int categoryId = 1;
            Optional<Category> optionalBefore = categoryRepository.findById(categoryId);
            if (optionalBefore.isEmpty()) {
                fail();
            }
            Category before = optionalBefore.get();
            System.out.println("before.getParentId() = " + before.getParentId());

            // 수정
            // To Do: 나중에 Category 에 도메인 메서드 구현되면, rebuild 말고 그 메서드 사용하자
            Category updatedRebuild = Category.rebuild(
                    before.getId(),
                    before.getName() + "(추가 내용)",
                    before.getLevel(),
                    before.getParentId()
            );
            categoryRepository.update(updatedRebuild);
            Optional<Category> optionalUpdated = categoryRepository.findById(categoryId);
            if (optionalUpdated.isEmpty()) {
                fail();
            }
            Category updated = optionalUpdated.get();
            
            // assert
            assertEquals(updated.getName(), before.getName() + "(추가 내용)");
            System.out.println("updated.getName() = " + updated.getName());

            conn.commit();
        } catch (SQLException e) {
            rollback(e, conn);
        }
    }

    @Test
    void delete() {
        try {

            // 새로운 카테고리 생성
            Category newCategory = Category.create(
                    "새로운 카테고리입니다. ",
                    CategoryLevel.ONE,
                    null
            );
            Category created
                    = categoryRepository.create(newCategory);
            int createdCategoryId = created.getId();

            // 방금 생성한 카테고리 생성 후 조회하면 결과 없음
            categoryRepository.delete(createdCategoryId);
            assertThrows(NoSuchElementException.class, () -> {
                categoryRepository.findById(createdCategoryId).get();
            });

            conn.commit();
        } catch (SQLException e) {
            rollback(e, conn);
        }
    }

    private static void printCategory(Category category) {
        System.out.println("Category{" +
                "id=" + category.getId() +
                ", name='" + category.getName() + '\'' +
                ", level=" + category.getLevel() +
                ", parentId=" + category.getParentId() +
                '}');
    }
}