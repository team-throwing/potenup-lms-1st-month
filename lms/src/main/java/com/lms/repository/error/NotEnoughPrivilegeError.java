package com.lms.repository.error;

/**
 * 권한 불충분
 */
public class NotEnoughPrivilegeError extends DatabaseAccessError {
    public NotEnoughPrivilegeError(String message, Throwable cause) {
        super(message, cause);
    }
}
