package com.lms.repository.exception;

/**
 * DBMS 내에서 트랜잭션 실패
 */
public class TransactionException extends DatabaseException {
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
