package com.lms.repository.exception;

import lombok.NoArgsConstructor;

/**
 * 대상 데이터베이스 레코드를 찾을 수 없는 경우 발생합니다.
 */
@NoArgsConstructor
public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String message) {
        super(message);
    }
}
