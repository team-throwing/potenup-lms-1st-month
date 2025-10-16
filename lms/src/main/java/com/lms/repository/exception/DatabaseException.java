package com.lms.repository.exception;

/**
 * 복구 가능한 데이터베이스 예외(unchecked)
 * <ul>
 *     <li>무결성 제약 조건 위반 (ex. 중복 키 삽입 시도, Not Null 위반, ...)</li>
 *     <li>잘못된 데이터 형식 (ex. 너무 긴 문자열, 오버 플로우, 언더 플로우, 잘못된 날짜 형식, ...)</li>
 *     <li>트랜잭션 실패 (ex. 데드락 감지, 락 대기시간 초과, ...)</li>
 *     <li>등등...</li>
 * </ul>
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
