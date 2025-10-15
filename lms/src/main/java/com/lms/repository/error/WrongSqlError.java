package com.lms.repository.error;

/**
 * 데이터베이스 접근 계층에서 SQL 잘못 작성
 */
public class WrongSqlError extends DatabaseError {
    public WrongSqlError(String message, Throwable cause) {
        super(message, cause);
    }
}
