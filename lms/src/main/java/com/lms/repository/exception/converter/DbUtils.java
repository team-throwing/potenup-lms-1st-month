package com.lms.repository.exception.converter;

import com.lms.repository.exception.DatabaseException;
import com.lms.repository.exception.error.DatabaseError;

import java.sql.Connection;
import java.sql.SQLException;

public class DbUtils {

    private final DbDialectExceptionConverter exceptionConverter;

    public DbUtils(DbDialectExceptionConverter exceptionConverter) {
        this.exceptionConverter = exceptionConverter;
    }

    /**
     * <ul>
     *     <li>복구 불가능한 DatabaseError 발생 시, 트랜잭션 롤백 후 커넥션을 반환하고 exit</li>
     *     <li>복구 가능한 unchecked DatabaseException 발생 시, throw</li>
     * </ul>
     * @param conn 커넥션
     * @param e JDBC 에서 발생한 SQLException
     */
    public void handleSQLException(Connection conn, SQLException e) {

        Throwable throwable = exceptionConverter.convert(e);

        // 복구 불가능한 오류인 경우
        if (throwable instanceof DatabaseError databaseError) {

            System.out.println(databaseError.getMessage());
            databaseError.printStackTrace();

            try {
                conn.rollback();
                conn.close();
            } catch (SQLException sqlException) {
                // 트랜잭션 롤백 또는 Connection 해제 실패
                sqlException.printStackTrace();
            }

            throw databaseError;
        }

        // 복구 가능한 예외인 경우
        if (throwable instanceof DatabaseException databaseException) {
            throw databaseException;
        }
    }
}
