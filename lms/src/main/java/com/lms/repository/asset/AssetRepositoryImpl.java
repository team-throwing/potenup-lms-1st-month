package com.lms.repository.asset;

import com.lms.domain.asset.Asset;
import com.lms.domain.asset.rebuild.RebuildAsset;
import com.lms.repository.exception.DatabaseException;
import com.lms.repository.exception.ModificationTargetNotFoundException;
import com.lms.repository.exception.converter.DbUtils;
import com.lms.service.ConnectionHolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssetRepositoryImpl implements AssetRepository {

    private final DbUtils dbUtils;

    public AssetRepositoryImpl(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    @Override
    public Asset create(Asset asset) {

        // 1. 파라미터 검증
        if (asset == null) {
            throw new IllegalArgumentException("asset 이 null 입니다.");
        }

        // 2. SQL 작성
        String sql = """
                INSERT INTO asset
                (mime_type, path, original_filename, converted_filename, content_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (1): create - executeUpdate -> t/f (with ResultSet for auto inc key)
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                // SQL 와일드 카드에 값 채우기
                pstmt.setString(1, asset.getMimeType());
                pstmt.setString(2, asset.getPath());
                pstmt.setString(3, asset.getOriginalFilename());
                pstmt.setString(4, asset.getConvertedFilename());
                pstmt.setLong(5, asset.getContentId());

                // SQL 실행
                boolean isNotCompleted = pstmt.executeUpdate() <= 0;
                if (isNotCompleted) {
                    throw new DatabaseException("Asset 생성 실패", null);
                }

                // 결과를 적절하게 처리
                Asset ret = null;
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        RebuildAsset rebuildAsset = new RebuildAsset(
                                rs.getLong(1),
                                asset.getMimeType(),
                                asset.getPath(),
                                asset.getOriginalFilename(),
                                asset.getConvertedFilename(),
                                asset.getContentId(),
                                asset.getStatus()
                        );

                        ret = Asset.rebuild(rebuildAsset);
                    }
                }

                return ret;
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        // x. 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
        throw new RuntimeException("CategoryRepositoryImpl.create: 도달 불가능하도록 의도된 지점에 도달했습니다.");
    }

    @Override
    public List<Asset> createAll(List<Asset> assets) {

        // 1. 파라미터 검증
        if (assets == null || assets.isEmpty()) {
            throw new IllegalArgumentException("assets 가 null empty");
        }

        // 2. SQL 작성
        String sql = """
                INSERT INTO asset(mime_type, path, original_filename, converted_filename, content_id)
                VALUES(?, ?, ?, ?, ?)
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (1): create - executeUpdate -> t/f (with ResultSet for auto inc key)
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                // SQL 와일드 카드에 값 채우기
                for (Asset asset : assets) {
                    pstmt.setString(1, asset.getMimeType());
                    pstmt.setString(2, asset.getPath());
                    pstmt.setString(3, asset.getOriginalFilename());
                    pstmt.setString(4, asset.getConvertedFilename());
                    pstmt.setLong(5, asset.getContentId());
                    pstmt.addBatch();
                }

                // SQL 실행
                int[] createResults = pstmt.executeBatch();

                // 결과를 적절하게 처리
                List<Long> assetIds = new ArrayList<>();
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    while (rs.next()) {
                        assetIds.add(rs.getLong(1));
                    }
                }

                List<Asset> ret = new ArrayList<>();
                for (int i = 0; i < assets.size(); i++) {
                    Asset curAsset = assets.get(i);
                    ret.add(Asset.rebuild(new RebuildAsset(
                            assetIds.add(assetIds.get(i)),
                            curAsset.getMimeType(),
                            curAsset.getPath(),
                            curAsset.getOriginalFilename(),
                            curAsset.getConvertedFilename(),
                            curAsset.getContentId(),
                            curAsset.getStatus()
                    )));
                }

                return ret;
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        // x. 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
        throw new RuntimeException("CategoryRepositoryImpl.create: 도달 불가능하도록 의도된 지점에 도달했습니다.");
    }

    @Override
    public List<Asset> findAll(long contentId) {

        // 1. 파라미터 검증
        if (contentId < 0) {
            throw new IllegalArgumentException("contentId 가 음수입니다.");
        }

        // 2. SQL 작성
        String sql = """
                SELECT (id, mime_type, path, original_filename, converted_filename, content_id)
                FROM asset
                WHERE content_id=?
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (3): select- executeQuery -> ResultSet
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // SQL 와일드 카드에 값 채우기
                pstmt.setLong(1, contentId);

                // SQL 실행
                List<Asset> ret = new ArrayList<>();
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        ret.add(Asset.rebuild(new RebuildAsset(
                                rs.getLong("id"),
                                rs.getString("mime_type"),
                                rs.getString("path"),
                                rs.getString("original_filename"),
                                rs.getString("converted_filename"),
                                rs.getLong("content_id")
                        )));
                    }
                }

                // 결과를 적절하게 처리
                return ret;
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        // x. 코드가 의도대로 올바르게 구현된 경우 여기에 도달할 수 없습니다.
        throw new RuntimeException("CategoryRepositoryImpl.create: 도달 불가능하도록 의도된 지점에 도달했습니다.");
    }

    @Override
    public void update(Asset asset) {

        // 1. 파라미터 검증
        if (asset == null) {
            throw new IllegalArgumentException("asset 이 null");
        }

        // 2. SQL 작성
        String sql = """
                UPDATE asset
                SET
                    mime_type=?,
                    path=?,
                    original_filename=?,
                    converted_filename=?,
                    content_id=?
                WHERE id=?
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (2): update | delete - executeUpdate -> affectedRowCount (no ResultSet)
            try (PreparedStatement pstmt = conn.prepareStatement(sql /*, Statement.RETURN_GENERATED_KEYS*/)) {

                // SQL 와일드 카드에 값 채우기
                pstmt.setString(1, asset.getMimeType());
                pstmt.setString(2, asset.getPath());
                pstmt.setString(3, asset.getOriginalFilename());
                pstmt.setString(4, asset.getConvertedFilename());
                pstmt.setLong(5, asset.getContentId());

                // SQL 실행
                boolean isNotCompleted = pstmt.executeUpdate() <= 0;
                if (isNotCompleted) {
                    throw new ModificationTargetNotFoundException("수정할 에셋을 찾지 못했습니다.", null);
                }
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }
    }

    @Override
    public void delete(long id) {

        // 1. 파라미터 검증
        if (id < 0) {
            throw new IllegalArgumentException("id 가 음수");
        }

        // 2. SQL 작성
        String sql = """
                DELETE FROM asset
                WHERE id=?
                """;

        // 3. Connection 객체 획득(Connection 객체는 여기서 닫으면 안 됨!)
        Connection conn = ConnectionHolder.get();

        // 4. JDBC 를 통해 SQL 문 실행 및 결과 처리
        try {

            // a. 처리 유형 (2): update | delete - executeUpdate -> affectedRowCount (no ResultSet)
            try (PreparedStatement pstmt = conn.prepareStatement(sql /*, Statement.RETURN_GENERATED_KEYS*/)) {

                // SQL 와일드 카드에 값 채우기
                pstmt.setLong(1, id);

                // SQL 실행
                boolean isNotCompleted = pstmt.executeUpdate() <= 0;
                if (isNotCompleted) {
                    throw new ModificationTargetNotFoundException("삭제할 에셋을 찾지 못했습니다.", null);
                }
            }
            // b. 예외 처리
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }
    }
}
