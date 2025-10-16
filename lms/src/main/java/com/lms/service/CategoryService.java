package com.lms.service;
import com.lms.dto.CategoryRequestDto;
import com.lms.domain.category.Category;
import com.lms.repository.category.CategoryRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // --------------------------
    // DTO → 엔티티 변환
    // --------------------------
    private Category toEntity(CategoryRequestDto dto) {
        if (dto.getId() == null) {
            // 새 엔티티 생성 (create()는 private이므로 직접 new)
            return new Category(
                    null,               // ID는 DB에서 자동 생성
                    dto.getName(),
                    dto.getLevel(),
                    dto.getParentId()
            );
        } else {
            // 기존 엔티티 rebuild 느낌
            return new Category(
                    dto.getId(),
                    dto.getName(),
                    dto.getLevel(),
                    dto.getParentId()
            );
        }
    }

    // --------------------------
    // 1️⃣ 생성
    // --------------------------
    public void createCategory(CategoryRequestDto dto) {
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            ConnectionHolder.set(conn);
            conn.setAutoCommit(false);

            if (dto.getName() == null || dto.getName().isEmpty()) {
                throw new IllegalArgumentException("이름은 필수입니다.");
            }

            if (dto.getId() != null && categoryRepository.findById(conn, dto.getId()).isPresent()) {
                throw new IllegalStateException("이미 존재하는 카테고리입니다.");
            }

            categoryRepository.create(toEntity(dto));

            conn.commit();
        } catch (Exception e) {
            if (conn != null) try {
                conn.rollback();
            } catch (Exception ex) {
            }
            throw new RuntimeException(e);
        } finally {
            ConnectionHolder.clear();
            if (conn != null) try {
                conn.close();
            } catch (Exception ex) {
            }
        }
    }

    // --------------------------
    // 2️⃣ 수정
    // --------------------------
    public void updateCategory(CategoryRequestDto dto) {
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            ConnectionHolder.set(conn);
            conn.setAutoCommit(false);

            if (dto.getId() == null) throw new IllegalArgumentException("ID는 필수입니다.");

            Category category = categoryRepository.findById(conn, dto.getId())
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 카테고리"));

            if (dto.getName() != null && !dto.getName().isEmpty()) {
                category.rebuild(dto.getName());
            }

            categoryRepository.update(category);
            conn.commit();
        } catch (Exception e) {
            if (conn != null) try {
                conn.rollback();
            } catch (Exception ex) {
            }
            throw new RuntimeException(e);
        } finally {
            ConnectionHolder.clear();
            if (conn != null) try {
                conn.close();
            } catch (Exception ex) {
            }
        }
    }

    // --------------------------
    // 3️⃣ 삭제
    // --------------------------
    public void deleteCategory(Long id) {
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            ConnectionHolder.set(conn);
            conn.setAutoCommit(false);

            if (id == null) throw new IllegalArgumentException("ID는 필수");

            if (categoryRepository.findById(id).isEmpty())
                throw new IllegalStateException("존재하지 않는 카테고리");

            categoryRepository.delete(id);
            conn.commit();
        } catch (Exception e) {
            if (conn != null) try {
                conn.rollback();
            } catch (Exception ex) {
            }
            throw new RuntimeException(e);
        } finally {
            ConnectionHolder.clear();
            if (conn != null) try {
                conn.close();
            } catch (Exception ex) {
            }
        }
    }

    // --------------------------
    // 4️⃣ 조회 (단일)
    // --------------------------
    public Category findCategory(Long id) throws SQLException {
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            ConnectionHolder.set(conn);

            if (id == null) throw new IllegalArgumentException("ID는 필수");

            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 카테고리"));

            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionHolder.clear();
            if (conn != null) try {
                conn.close();
            } catch (Exception ex) {
            }
        }
    }
}
