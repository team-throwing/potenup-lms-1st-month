package com.lms.repository.exception.error;

/**
 * SQL 문법 오류
 */
public class SqlSyntaxError extends AccessAndGeneralSqlError {
    public SqlSyntaxError(String message, Throwable cause) {
        super(message, cause);
    }
}
