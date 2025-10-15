package com.lms.repository.exception;

/**
 * DBMS 에서 데드락이 감지됨
 */
public class DeadlockException extends TransactionException {
    public DeadlockException(String message, Throwable cause) {
        super(message, cause);
    }
}
