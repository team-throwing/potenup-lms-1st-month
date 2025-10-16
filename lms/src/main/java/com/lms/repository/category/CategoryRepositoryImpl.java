package com.lms.repository.category;

import com.lms.domain.category.Category;
import com.lms.domain.category.CategoryLevel;
import com.lms.service.ConnectionHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CategoryRepositoryImpl implements CategoryRepository {

    @Override
    public void create(Category category) {

        if (category == null) {
            throw new IllegalArgumentException("category 가 null 입니다.");
        }

        if (category.getId() != null) {
            throw new IllegalArgumentException("category.id 가 null 이 아닙니다.");
        }

        String sql = """
                INSERT INTO category
                (name, level, parent_id)
                VALUES ();
                """;

        try {

            // Connection 객체는 닫으면 안 됨
            Connection conn = ConnectionHolder.get();

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, category.getName());
                pstmt.setString(2, category.getLevel().name());
                pstmt.setInt(3, category.getParentId());

                int result = pstmt.executeUpdate();
                if (result == 0) {
                    throw new RuntimeException("알 수 없는 이유로 생성에 실패했습니다.");
                }
            }
        } catch (SQLException e) {
            // To Do: 예외 처리 로직 메서드로 빼자
            // DbDialectExceptionConverter.convert 메서드 사용
            // 1. 복구 불가능한 오류인 경우 오류 발생
            // 2. 복구 가능한 데이터베이스 예외 던지기
        }
    }

    @Override
    public Category findById(long id) {
        return null;
    }

    @Override
    public List<Category> findAllByCategoryLevel(CategoryLevel categoryLevel) {
        return null;
    }

    @Override
    public Category findParentByChildId(long id) {
        return null;
    }

    @Override
    public List<Category> findChildrenByParentId(long id) {
        return null;
    }

    @Override
    public List<Category> findAll() {
        return null;
    }

    @Override
    public void update(Category category) {

    }

    @Override
    public void delete(long id) {

    }
}
