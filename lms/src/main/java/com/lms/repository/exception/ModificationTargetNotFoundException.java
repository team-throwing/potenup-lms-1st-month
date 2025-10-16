package com.lms.repository.exception;

public class ModificationTargetNotFoundException extends DatabaseException {
    public ModificationTargetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
