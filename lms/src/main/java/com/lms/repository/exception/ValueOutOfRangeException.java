package com.lms.repository.exception;

/**
 * 값 범위 초과(언더 & 오버 플로우)
 */
public class ValueOutOfRangeException extends DataException {
    public ValueOutOfRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
