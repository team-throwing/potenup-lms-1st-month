package com.lms.repository.exception;

/**
 * 락 대기 시간 초과
 */
public class LockWaitingTimeoutException extends TransactionException {
    public LockWaitingTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
