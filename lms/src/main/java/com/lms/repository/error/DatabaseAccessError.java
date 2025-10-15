package com.lms.repository.error;

/**
 * 데이터베이스 연결 및 자원 접근 관련 오류
 */
public class DatabaseAccessError extends DatabaseError {
    public DatabaseAccessError(String message, Throwable cause) {
        super(message, cause);
    }
}
