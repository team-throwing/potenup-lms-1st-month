package com.lms.repository.exception.error;

/**
 * 데이터베이스 접근 계층에서 SQL 잘못 작성
 */
public class AccessAndGeneralSqlError extends DatabaseError {
    public AccessAndGeneralSqlError(String message, Throwable cause) {
        super(message, cause);
    }
}
