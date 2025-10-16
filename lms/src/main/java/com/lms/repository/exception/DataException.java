package com.lms.repository.exception;

/**
 * 데이터베이스에 삽입되는 값 형식 관련 오류
 */
public class DataException extends  DatabaseException {
    public DataException(String message, Throwable cause) {
        super(message, cause);
    }
}
