package com.lms.repository.error;

/**
 * 컬럼 타입에 맞지 않는 값을 할당 시도
 */
public class ColumnTypeNotMatchError extends WrongSqlError {
    public ColumnTypeNotMatchError(String message, Throwable cause) {
        super(message, cause);
    }
}
