package com.lms.repository.exception;

/**
 * NOT NULL 이고 DEFAULT 값이 없는 컬럼에 값을 미할당
 */
public class MissingDefaultValueException extends ColumnCannotBeNullException {
    public MissingDefaultValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
