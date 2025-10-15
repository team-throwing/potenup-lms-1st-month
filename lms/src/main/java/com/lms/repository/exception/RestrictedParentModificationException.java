package com.lms.repository.exception;

/**
 * 참조하는 자식 레코드가 남아있는, 부모 레코드를 삭제하거나 부모 레코드의 기본키를 수정하려 할 때 발생
 */
public class RestrictedParentModificationException extends ForeignKeyConstraintException {
    public RestrictedParentModificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
