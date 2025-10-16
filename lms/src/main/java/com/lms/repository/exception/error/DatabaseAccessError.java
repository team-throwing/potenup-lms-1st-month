package com.lms.repository.exception.error;

/**
 * 데이터베이스 연결 문제
 */
public class DatabaseAccessError extends DatabaseError {
    public DatabaseAccessError(String message, Throwable cause) {
        super(message, cause);
    }
}
