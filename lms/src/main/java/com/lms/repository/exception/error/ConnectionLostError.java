package com.lms.repository.exception.error;

/**
 * DB 가 연결을 끊거나 통신 오류 발생
 */
public class ConnectionLostError extends DatabaseAccessError {
    public ConnectionLostError(String message, Throwable cause) {
        super(message, cause);
    }
}
