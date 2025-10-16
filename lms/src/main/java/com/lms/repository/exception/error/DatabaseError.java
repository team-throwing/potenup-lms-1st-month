package com.lms.repository.exception.error;

/**
 * 복구 불가능한 데이터베이스 오류
 */
public class DatabaseError extends Error {
    public DatabaseError(String message, Throwable cause) {
        super(message, cause);
    }
}
