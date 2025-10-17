package com.lms.repository.category;

import com.lms.domain.category.Category;
import com.lms.domain.category.CategoryLevel;
import com.lms.repository.exception.ModificationTargetNotFoundException;
import com.lms.repository.exception.converter.DbUtils;
import com.lms.service.ConnectionHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final DbUtils dbUtils;

    public CategoryRepositoryImpl(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    @Override
    public Category create(Category category) {

        // 1. 파라미터 검증
        if (category == null) {
            throw new IllegalArgumentException("category 가 null 입니다.");
        }
        if (category.getId() != null) {
            throw new IllegalArgumentException("category.id 가 null 이 아닙니다.");
        }

        // 2. SQL 문 작성
        String sql = """
                INSERT INTO category
                (name, level, parent_id)
                VALUES ();
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (1): create - executeUpdate with auto inc key
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                // SQL 와일드 카드에 값 채우기
                pstmt.setString(1, category.getName());
                pstmt.setString(2, category.getLevel().name());
                pstmt.setInt(3, category.getParentId());

                // SQL 실행
                int result = pstmt.executeUpdate();
                if (result == 0) {
                    throw new RuntimeException("알 수 없는 이유로 생성에 실패했습니다.");
                }

                // 결과를 적절하게 처리
                Category created = null;
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        long id = rs.getLong(1);
                        created = Category.rebuild(
                                (int) id,
                                category.getName(),
                                category.getLevel(),
                                category.getParentId()
                        );
                    }
                }
                return created;
            }

            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        // x. 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
        throw new RuntimeException("CategoryRepositoryImpl.create: 도달 불가능하도록 의도된 지점에 도달했습니다.");
    }

    @Override
    public Optional<Category> findById(int id) {

        // 1. 파라미터 검증
        if (id < 0) {
            throw new IllegalArgumentException("id 가 음수입니다.");
        }

        // 2. SQL 작성
        String sql = """
                SELECT id, name, parent_id, level
                FROM category
                WHERE id=?
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (3): select - executeQuery -> ResultSet
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // SQL 와일드 카드에 값 채우기
                pstmt.setInt(1, id);

                // SQL 실행
                Category found = null;
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        found = Category.rebuild(
                                // id
                                rs.getInt("id"),
                                // name
                                rs.getString("name"),
                                // level
                                rs.getString("level").equals("ONE")
                                        ? CategoryLevel.ONE : CategoryLevel.TWO,
                                // parent id
                                rs.getInt("parent_id")
                        );
                    }
                }

                // 결과를 반환
                return Optional.ofNullable(found);
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        // x. 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
        throw new RuntimeException("CategoryRepositoryImpl.create: 도달 불가능하도록 의도된 지점에 도달했습니다.");
    }

    @Override
    public List<Category> findAllByCategoryLevel(CategoryLevel categoryLevel) {

        // 1. 파라미터 검증
        if (categoryLevel == null) {
            throw new IllegalArgumentException("categoryLevel 이 null 입니다.");
        }

        // 2. SQL 작성
        String sql = """
                SELECT id, name, parent_id, level
                FROM category
                WHERE level=?
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (3): select - executeQuery -> ResultSet
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // SQL 와일드 카드에 값 채우기
                pstmt.setString(1, categoryLevel.name());

                // SQL 실행
                List<Category> found = new ArrayList<>();
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Category aRecordFound = Category.rebuild(
                                // id
                                rs.getInt("id"),
                                // name
                                rs.getString("name"),
                                // level
                                rs.getString("level").equals("ONE")
                                        ? CategoryLevel.ONE : CategoryLevel.TWO,
                                // parent id
                                rs.getInt("parent_id")
                        );

                        found.add(aRecordFound);
                    }
                }

                // 결과를 반환
                return found;
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        // x. 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
        throw new RuntimeException("CategoryRepositoryImpl.create: 도달 불가능하도록 의도된 지점에 도달했습니다.");
    }

    @Override
    public List<Category> findChildrenByParentId(int id) {

        // 1. 파라미터 검증
        if (id < 0) {
            throw new IllegalArgumentException("id 가 음수입니다.");
        }

        // 2. SQL 작성
        String sql = """
                SELECT id, name, parent_id, level
                FROM category
                WHERE parent_id=?
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (3): select - executeQuery -> ResultSet
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // SQL 와일드 카드에 값 채우기
                pstmt.setInt(1, id);

                // SQL 실행
                List<Category> found = new ArrayList<>();
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Category aRecordFound = Category.rebuild(
                                // id
                                rs.getInt("id"),
                                // name
                                rs.getString("name"),
                                // level
                                rs.getString("level").equals("ONE")
                                        ? CategoryLevel.ONE : CategoryLevel.TWO,
                                // parent id
                                rs.getInt("parent_id")
                        );

                        found.add(aRecordFound);
                    }
                }

                // 결과를 반환
                return found;
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        // x. 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
        throw new RuntimeException("CategoryRepositoryImpl.create: 도달 불가능하도록 의도된 지점에 도달했습니다.");
    }

    @Override
    public List<Category> findAll() {

        // 1. SQL 작성
        String sql = """
                SELECT id, name, parent_id, level
                FROM category
                """;

        // 2. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 3. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (3): select - executeQuery -> ResultSet
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // SQL 실행
                List<Category> found = new ArrayList<>();
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Category aRecordFound = Category.rebuild(
                                // id
                                rs.getInt("id"),
                                // name
                                rs.getString("name"),
                                // level
                                rs.getString("level").equals("ONE")
                                        ? CategoryLevel.ONE : CategoryLevel.TWO,
                                // parent id
                                rs.getInt("parent_id")
                        );

                        found.add(aRecordFound);
                    }
                }

                // 결과를 반환
                return found;
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        // x. 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
        throw new RuntimeException("CategoryRepositoryImpl.create: 도달 불가능하도록 의도된 지점에 도달했습니다.");
    }

    @Override
    public void update(Category category) {

        // 1. 파라미터 검증
        if (category == null) {
            throw new IllegalArgumentException("category 가 null 입니다.");
        }
        if (category.getId() == null) {
            throw new IllegalArgumentException("category.getId() 가 null 입니다.");
        }

        // 2. SQL 작성
        String sql = """
                UPDATE category
                SET
                    name=?,
                    level=?,
                    parent_id=?
                WHERE id=?
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (2): update | delete - executeUpdate -> affectedRowCount (no ResultSet)
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // SQL 와일드 카드에 값 채우기
                pstmt.setString(1, category.getName());
                pstmt.setString(2, category.getLevel().name());
                pstmt.setInt(3, category.getParentId());
                pstmt.setInt(4, category.getId());

                // SQL 실행
                boolean isNotUpdated = pstmt.executeUpdate(sql) <= 0;

                // 결과를 적절하게 처리
                if (isNotUpdated) {
                    throw new ModificationTargetNotFoundException("update 할 category 를 찾지 못했습니다.", null);
                }
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }
    }

    @Override
    public void delete(int id) {

        // 1. 파라미터 검증
        if (id < 0) {
            throw new IllegalArgumentException("id 가 음수입니다.");
        }

        // 2. SQL 작성
        String sql = """
                DELETE FROM category
                WHERE id=?
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (2): update | delete - executeUpdate -> affectedRowCount (no ResultSet)
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // SQL 와일드 카드에 값 채우기
                pstmt.setInt(1, id);

                // SQL 실행
                boolean isNotDeleted = pstmt.executeUpdate(sql) <= 0;

                // 결과를 적절하게 처리
                if (isNotDeleted) {
                    throw new ModificationTargetNotFoundException("delete 할 category 를 찾지 못했습니다.", null);
                }
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }
    }
}
