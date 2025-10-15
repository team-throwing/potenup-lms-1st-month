package com.lms.repository.exception;

/**
 * 명시된 부모 키에 해당하는 부모 레코드가 존재하지 않음
 */
public class ParentNotFoundException extends ForeignKeyConstraintException {
    public ParentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
