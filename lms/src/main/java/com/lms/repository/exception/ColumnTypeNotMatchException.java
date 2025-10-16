package com.lms.repository.exception;

/**
 * 컬럼 타입에 맞지 않는 값을 할당 시도
 */
public class ColumnTypeNotMatchException extends DataException {
    public ColumnTypeNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
