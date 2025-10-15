package com.lms.repository.exception.error;

/**
 * 컬럼 이름을 찾을 수 없음
 */
public class ColumnNotFoundError extends WrongSqlError {
    public ColumnNotFoundError(String message, Throwable cause) {
        super(message, cause);
    }
}
