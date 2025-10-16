
package com.lms.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * HikariCP 기반 DB 커넥션 풀 관리
 */
public class DataSource {

    // static final로 한 번만 초기화
    private static final HikariDataSource ds;

    static {
        try {
            HikariConfig config = new HikariConfig();
            // MySQL URL 예시 (DB 이름 ims, 시간대 설정 포함)
            config.setJdbcUrl("jdbc:mysql://localhost:3306/ims?serverTimezone=UTC");
            config.setUsername("root");       // DB 계정
            config.setPassword("1234");       // DB 비밀번호
            config.setMaximumPoolSize(10);    // 풀 최대 커넥션 수
            config.setAutoCommit(false);      // Service에서 commit/rollback 처리

            ds = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("HikariCP 초기화 실패", e);
        }
    }

    /**
     * Service/DAO에서 사용할 Connection 제공
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = ds.getConnection();
        conn.setAutoCommit(false); // 안전하게 트랜잭션 제어
        return conn;
    }
}
