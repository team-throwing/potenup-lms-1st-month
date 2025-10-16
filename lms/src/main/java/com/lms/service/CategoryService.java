package com.lms.service;
import com.lms.dto.CategoryRequestDto; // dto 있다는 가정
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


    // DTO → 엔티티 변환

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


    //  생성
    public void createCategory(CategoryRequestDto dto) {
        Connection conn = null; // 기본 연결상태 null
        try {
            conn = DataSource.getConnection(); // 단일 DAO라서 굳이 필요없지만 일단 넣음
            ConnectionHolder.set(conn);
            conn.setAutoCommit(false);

            if (dto.getName() == null || dto.getName().isEmpty()) {  //dto에서 받아오는 이름이 null이거나 비어있을때)
                throw new IllegalArgumentException("이름은 필수입니다.");
            }

            if (dto.getId() != null && categoryRepository.findById(conn, dto.getId()).isPresent()) {
                throw new IllegalStateException("이미 존재하는 카테고리입니다."); //dto로 받은 id가 null이 아니고 현재 존재할때
            }

            categoryRepository.create(toEntity(dto)); // DTO를 엔티티로 변환한 뒤, CategoryRepository를 통해 DB에 저장

            conn.commit();
        } catch (Exception e) {
            if (conn != null) try {
                conn.rollback();
            } catch (Exception ex) {
            }
            throw new RuntimeException(e);  // 예외 발생 시 트랜잭션 롤백 후, RuntimeException으로 재던짐
        } finally {
            ConnectionHolder.clear();
            if (conn != null) try {
                conn.close();  // 커넥션 홀더 초기화 및 DB 연결 종료
            } catch (Exception ex) {
            }
        }
    }
    //  수정

    public void updateCategory(CategoryRequestDto dto) {
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            ConnectionHolder.set(conn);
            conn.setAutoCommit(false);

            if (dto.getId() == null) throw new IllegalArgumentException("ID는 필수입니다.");

            // ID로 카테고리를 조회하고, 존재하지 않으면 예외 발생
            Category category = categoryRepository.findById(conn, dto.getId())
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 카테고리"));

            // 받은 이름이 NULL이 아니고 빈 문자열이 아닐때
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

    //  삭제

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

 //조회
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
