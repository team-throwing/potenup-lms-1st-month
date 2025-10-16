package com.lms.repository.exception.error;

/**
 * 테이블 또는 뷰 이름을 찾을 수 없는 경우에 발생하는 오류
 */
public class TableOrViewNotFoundError extends AccessAndGeneralSqlError {
    public TableOrViewNotFoundError(String message, Throwable cause) {
        super(message, cause);
    }
}
