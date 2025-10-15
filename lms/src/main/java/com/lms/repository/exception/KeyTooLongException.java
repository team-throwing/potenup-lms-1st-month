package com.lms.repository.exception;

/**
 * 기본키 범위 초과
 */
public class KeyTooLongException extends ConstraintViolationException {
    public KeyTooLongException(String message, Throwable cause) {
        super(message, cause);
    }
}
