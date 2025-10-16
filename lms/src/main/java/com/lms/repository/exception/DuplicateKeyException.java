package com.lms.repository.exception;

/**
 * 중복된 기본키
 */
public class DuplicateKeyException extends ConstraintViolationException {
    public DuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
