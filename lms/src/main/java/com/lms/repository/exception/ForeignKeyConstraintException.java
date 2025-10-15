package com.lms.repository.exception;

/**
 * 외래키 제약조건 위반
 */
public class ForeignKeyConstraintException extends ConstraintViolationException {
    public ForeignKeyConstraintException(String message, Throwable cause) {
        super(message, cause);
    }
}
