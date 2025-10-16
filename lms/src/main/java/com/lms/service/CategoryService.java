package com.lms.service;

import com.lms.domain.category.Category;
import com.lms.repository.category.CategoryRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }  // 카테고리 리포지토리 선언 초기화


    // 기본 커넥션 null
    public void createCategory(CategoryDto dto) throws SQLException {
        Connection conn = null;
        if (dto == null) {
            throw new IllegalArgumentException("CategoryDto 가 유효하지 않습니다.");
        }
        try {
            conn = DataSource.getConnection(); // 커넥션 객체 생성
            ConnectionHolder.set(conn);
            conn.setAutoCommit(false);

            Category category = toEntity(dto);            // DTO → Entity 변환
            categoryRepository.create(category);            // DAO 호출
            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                }
            }
            throw new RuntimeException(e);
        } finally {
            ConnectionHolder.clear();
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    public List<Category> findAll() throws SQLException {
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            ConnectionHolder.set(conn);

            return categoryRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionHolder.clear();
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                }
            }
        }
    }


    public Category findById(int id) throws SQLException {
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            ConnectionHolder.set(conn);

            return categoryRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionHolder.clear();
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                }
            }
        }

    }


    public void update(CategoryDto dto) throws SQLException {
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            ConnectionHolder.set(conn);
            conn.setAutoCommit(false);

            Category category = toEntity(dto);
            categoryRepository.update(category);
            conn.commit();
        } catch (Exception e) {
            if (conn != null)
                try { conn.rollback(); }
                    catch(Exception ex) {}
                        throw new RuntimeException(e);
        }

    }


        public void delete (CategoryDto dto) throws SQLException {
            Connection conn = null;
            try {
                conn = DataSource.getConnection();
                ConnectionHolder.set(conn);
                conn.setAutoCommit(false);

                categoryRepository.delete(dto.getId());
                conn.commit();
            } catch (Exception e) {
                if (conn != null)
                    try { conn.rollback(); }
                        catch(Exception ex) {}
                            throw new RuntimeException(e);
            }

        }


    private Category toEntity(CategoryDto dto) {
        if (dto.getId() == null) {
            return new Category(
                    null,
                    dto.getName(),
                    dto.getLevel(),
                    dto.getParentId()
            );
        } else {
            return new Category(
                    dto.getId(),
                    dto.getName(),
                    dto.getLevel(),
                    dto.getParentId()
            );
        }
    }


}


