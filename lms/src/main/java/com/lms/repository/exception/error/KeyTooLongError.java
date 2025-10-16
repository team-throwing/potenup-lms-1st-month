package com.lms.repository.exception.error;

/**
 * 키 범위 초과
 */
public class KeyTooLongError extends AccessAndGeneralSqlError {
    public KeyTooLongError(String message, Throwable cause) {
        super(message, cause);
    }
}