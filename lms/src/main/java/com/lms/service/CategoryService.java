package com.lms.service;

import com.lms.dto.CategoryRequestDto;
import com.lms.domain.category.Category;
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

    // =========================
    // 생성
    // =========================
    public void createCategory(CategoryRequestDto dto) {
        try (Connection conn = DataSourceFactory.get().getConnection()) {
            conn.setAutoCommit(false);

            if (dto.getName() == null || dto.getName().isBlank())
                throw new IllegalArgumentException("카테고리 이름은 필수입니다.");

            if (dto.getId() != null && categoryRepository.findById(dto.getId()).isPresent())
                throw new IllegalStateException("이미 존재하는 카테고리입니다.");

            categoryRepository.create(toEntity(dto));
            conn.commit();

        } catch (DatabaseException e) {
            throw new DatabaseError("카테고리 생성 중 데이터베이스 오류 발생", e);
        } catch (SQLException e) {
            throw new DatabaseError("카테고리 생성 중 SQLException 발생", e);
        }
    }

    // =========================
    // 수정
    // =========================
    public void updateCategory(CategoryRequestDto dto) {
        try (Connection conn = DataSourceFactory.get().getConnection()) {
            conn.setAutoCommit(false);

            Category category = categoryRepository.findById(dto.getId())
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리입니다."));

            if (dto.getName() != null) category.changeName(dto.getName());
            if (dto.getLevel() != null) category.changeLevel(dto.getLevel());
            if (dto.getParentId() != null) category.changeParent(dto.getParentId());

            categoryRepository.update(category);
            conn.commit();
        } catch (Exception e) {
            throw new DatabaseError("카테고리 수정 중 오류 발생", e);
        }
    }


    // =========================
    // 삭제
    // =========================
    public void deleteCategory(int id) {
        try (Connection conn = DataSourceFactory.get().getConnection()) {
            conn.setAutoCommit(false);

            if (id < 0)
                throw new IllegalArgumentException("ID는 양수여야 합니다.");

            if (categoryRepository.findById(id).isEmpty())
                throw new NoSuchElementException("존재하지 않는 카테고리입니다.");

            categoryRepository.delete(id);
            conn.commit();

        } catch (DatabaseException e) {
            throw new DatabaseError("카테고리 삭제 중 데이터베이스 오류 발생", e);
        } catch (SQLException e) {
            throw new DatabaseError("카테고리 삭제 중 SQLException 발생", e);
        }
    }

    // =========================
    // 단건 조회
    // =========================
    public Category findCategory(int id) {
        if (id < 0) throw new IllegalArgumentException("ID는 양수여야 합니다.");
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리입니다."));
    }

    // =========================
    // 전체 조회
    // =========================
    public List<Category> findAllCategories() {
        try {
            return categoryRepository.findAll();
        } catch (DatabaseException e) {
            throw new DatabaseError("전체 카테고리 조회 중 데이터베이스 오류 발생", e);
        }
    }
}
