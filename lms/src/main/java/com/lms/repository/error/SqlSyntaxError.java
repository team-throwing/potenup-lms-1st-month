package com.lms.repository.error;

/**
 * SQL 문법 오류
 */
public class SqlSyntaxError extends WrongSqlError {
    public SqlSyntaxError(String message, Throwable cause) {
        super(message, cause);
    }
}
