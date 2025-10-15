package com.lms.repository.exception;

import lombok.NoArgsConstructor;

/**
 * 데이터베이스 제약사항을 어긴 경우 발생합니다.
 */
@NoArgsConstructor
public class ConstraintViolationException extends RuntimeException {
    public ConstraintViolationException(String message) {
        super(message);
    }
}