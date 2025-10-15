package com.lms.repository.exception;

/**
 * 복구 가능한 데이터베이스 예외(unchecked)
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
