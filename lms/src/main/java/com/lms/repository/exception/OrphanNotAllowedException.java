package com.lms.repository.exception;

/**
 * 레코드 삭제 시 고아 레코드가 발생하는 것이 허용되지 않은 경우 발생합니다.
 */
public class OrphanNotAllowedException extends RuntimeException {
    public OrphanNotAllowedException(String message) {
        super(message);
    }
}
