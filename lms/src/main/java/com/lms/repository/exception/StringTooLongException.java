package com.lms.repository.exception;

/**
 * 지정된 길이보다 긴 문자열 할당 시도
 */
public class StringTooLongException extends DataException {
    public StringTooLongException(String message, Throwable cause) {
        super(message, cause);
    }
}
