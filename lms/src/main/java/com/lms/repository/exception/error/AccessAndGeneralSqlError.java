package com.lms.repository.exception.error;

/**
 * 권한 문제 또는 일반적인 SQL 오류
 */
public class AccessAndGeneralSqlError extends DatabaseError {
    public AccessAndGeneralSqlError(String message, Throwable cause) {
        super(message, cause);
    }
}
