package com.lms.repository.exception;

/**
 * 무결성 제약조건 위반
 */
public class ConstraintViolationException extends DatabaseException {
    public ConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}