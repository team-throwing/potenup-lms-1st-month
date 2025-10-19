package com.lms.service;

import com.lms.service.dto.category.CreateCategoryDto;
import com.lms.domain.category.Category;
import com.lms.domain.category.CategoryLevel;
import com.lms.service.dto.category.UpdateCategoryDto;
import com.lms.repository.category.CategoryRepository;
import com.lms.repository.config.DataSourceFactory;
import com.lms.repository.config.RepositoryConfig;
import com.lms.repository.exception.DatabaseException;
import com.lms.repository.exception.error.DatabaseError;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class CategoryService {

    private final CategoryRepository categoryRepository = RepositoryConfig.categoryRepository();

    // 생성
    public void createCategory(CreateCategoryDto dto) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            categoryRepository.create(dto.toEntity());
            conn.commit();
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("카테고리 생성 중 오류 발생", e);
        } finally {
            closeSafely(conn);
        }
    }

    // 수정
    public void updateCategory(UpdateCategoryDto dto) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            Category category = categoryRepository.findById(dto.id())
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리입니다."));

            Optional.ofNullable(dto.name()).ifPresent(category::rename);
            Optional.ofNullable(dto.level()).ifPresent(category::updateLevel);
            Optional.ofNullable(dto.parentId()).ifPresent(category::updateParentId);

            categoryRepository.update(category);

            conn.commit();
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("카테고리 수정 중 오류 발생", e);
        } finally {
            closeSafely(conn);
        }
    }

    // 삭제
    public void deleteCategory(Integer id) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            categoryRepository.delete(id);
            conn.commit();
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("카테고리 삭제 중 오류 발생", e);
        } finally {
            closeSafely(conn);
        }
    }

    // 조회
    public Category findCategory(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리입니다."));
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> findCategoriesByLevel(CategoryLevel level) {
        try {
            return categoryRepository.findAllByCategoryLevel(level);
        } catch (DatabaseException e) {
            throw new DatabaseError("카테고리 레벨 조회 중 오류 발생", e);
        }
    }

    // 내부 유틸
    private void rollbackSafely(Connection conn) {
        if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
    }

    private void closeSafely(Connection conn) {
        ConnectionHolder.clear();
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
    }
}
