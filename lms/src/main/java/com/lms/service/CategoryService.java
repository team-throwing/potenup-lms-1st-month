package com.lms.service;

import com.lms.dto.CategoryRequestDto;
import com.lms.domain.category.Category;
import com.lms.domain.category.CategoryLevel;
import com.lms.repository.category.CategoryRepository;
import com.lms.repository.config.DataSourceFactory;
import com.lms.repository.config.RepositoryConfig;
import com.lms.repository.exception.DatabaseException;
import com.lms.repository.exception.error.DatabaseError;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public class CategoryService {

    private final CategoryRepository categoryRepository = RepositoryConfig.categoryRepository();

    private Category toEntity(CategoryRequestDto dto) {
        if (dto.getId() == null) {
            return Category.create(dto.getName(), dto.getLevel(), dto.getParentId());
        } else {
            return Category.rebuild(dto.getId(), dto.getName(), dto.getLevel(), dto.getParentId());
        }
    }

    // 생성
    public void createCategory(CategoryRequestDto dto) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            categoryRepository.create(toEntity(dto));
            conn.commit();
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("카테고리 생성 중 오류 발생", e);
        } finally {
            clearConnection();
            closeSafely(conn);
        }
    }

    // 수정
    public void updateCategory(CategoryRequestDto dto) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            Category category = categoryRepository.findById(dto.getId())
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리입니다."));
            if (dto.getName() != null) category.rename(dto.getName());
            if (dto.getLevel() != null) category.updateLevel(dto.getLevel());
            if (dto.getParentId() != null) category.updateParentId(dto.getParentId());

            categoryRepository.update(category);
            conn.commit();
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("카테고리 수정 중 오류 발생", e);
        } finally {
            clearConnection();
            closeSafely(conn);
        }
    }

    // 삭제
    public void deleteCategory(int id) {
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
            clearConnection();
            closeSafely(conn);
        }
    }

    // 조회
    public Category findCategory(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리입니다."));
    }

    public List<Category> findAllCategories() {
        try {
            return categoryRepository.findAll();
        } catch (DatabaseException e) {
            throw new DatabaseError("카테고리 전체 조회 중 오류 발생", e);
        }
    }

    public List<Category> findCategoriesByLevel(CategoryLevel level) {
        try {
            return categoryRepository.findAllByCategoryLevel(level);
        } catch (DatabaseException e) {
            throw new DatabaseError("카테고리 레벨 조회 중 오류 발생", e);
        }
    }

    public List<Category> findChildrenByParentId(int id) {
        try {
            return categoryRepository.findChildrenByParentId(id);
        } catch (DatabaseException e) {
            throw new DatabaseError("부모 카테고리로 하위카테고리 조회 중 오류 발생", e);
        }
    }


    // 내부 유틸
    private void rollbackSafely(Connection conn) {
        if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
    }

    private void clearConnection() {
        try { ConnectionHolder.clear(); } catch (Exception ignored) {}
    }

    private void closeSafely(Connection conn) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
    }
}
