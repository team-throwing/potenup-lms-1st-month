package com.lms.repository.exception;

/**
 * Not Null 필드에 값 미할당
 */
public class ColumnCannotBeNullException extends ConstraintViolationException {
    public ColumnCannotBeNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
