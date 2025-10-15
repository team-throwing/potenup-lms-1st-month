package com.lms.repository.error;

/**
 * 데이터베이스 연결 거부 됨
 */
public class ConnectionRejectedError extends DatabaseAccessError {
    public ConnectionRejectedError(String message, Throwable cause) {
        super(message, cause);
    }
}
