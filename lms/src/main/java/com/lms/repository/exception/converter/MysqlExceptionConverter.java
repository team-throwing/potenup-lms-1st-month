package com.lms.repository.exception.converter;

import com.lms.repository.exception.*;
import com.lms.repository.exception.error.*;

import java.sql.SQLException;

public class MysqlExceptionConverter implements DbDialectExceptionConverter {
    @Override
    public Throwable convert(SQLException sqlException) {

        if (sqlException == null) {
            throw new IllegalArgumentException("sqlException 이 null 입니다.");
        }

        String reason = sqlException.getMessage();
        String sqlState = sqlException.getSQLState();
        int vendorCode = sqlException.getErrorCode();

        // mysql vendor code 확인
        Throwable toBeThrown = getMysqlThrowable(vendorCode, sqlException);
        if (toBeThrown != null) {
            return toBeThrown;
        }

        // sqlstate 확인
        toBeThrown = getSqlThrowable(sqlState, sqlException);
        if (toBeThrown != null) {
            return toBeThrown;
        }

        return new DatabaseException(reason, sqlException);
    }

    private Throwable getSqlThrowable(String sqlState, Throwable cause) {
        Throwable ret = null;

        if (sqlState == null) {
            throw new IllegalArgumentException("sqlState 가 null 입니다.");
        }

        String sqlStateClass = sqlState.substring(0, 2);
        switch (sqlStateClass) {
            // 복구 불가능한 오류(Error)
            case "42" -> ret = new AccessAndGeneralSqlError(
                    "권한 문제 또는 일반적인 SQL 오류: " + cause.getMessage(), cause);
            case "08" -> ret = new DatabaseAccessError(
                    "데이터베이스 연결 문제: " + cause.getMessage(), cause);

            // 복구 가능한 예외(RuntimeException)
            case "23" -> ret = new ConstraintViolationException(
                    "무결성 제약조건 위반: " + cause.getMessage(), cause);
            case "22" -> ret = new DataException(
                    "잘못된 값 형식: " + cause.getMessage(), cause);
            case "40" -> ret = new TransactionException(
                    "DBMS 에서 트랜잭션 실패: " + cause.getMessage(), cause);
        }

        return ret;
    }

    private Throwable getMysqlThrowable(int vendorCode, Throwable cause) {
        Throwable ret = null;
        switch (vendorCode) {
            // 복구 불가능한 오류(Error)
            case 1064 -> ret = new SqlSyntaxError(
                    "SQL 구문 오류: " + cause.getMessage(), cause);
            case 1146 -> ret = new TableOrViewNotFoundError(
                    "테이블 또는 뷰 이름을 찾을 수 없음: " + cause.getMessage(), cause);
            case 1054 -> ret = new ColumnNotFoundError(
                    "컬럼 이름을 찾을 수 없음: " + cause.getMessage(), cause);
            case 1071 -> ret = new KeyTooLongError(
                    "키 범위 초과: " + cause.getMessage(), cause);
            case 1142, 1143 -> ret = new NotEnoughPrivilegeError(
                    "권한 불충분: " + cause.getMessage(), cause);
            case 1045 -> ret = new ConnectionRejectedError(
                    "데이터베이스 연결 거부 됨: " + cause.getMessage(), cause);
            case 2006 -> ret = new ConnectionLostError(
                    "DB 가 연결을 끊었거나 통신 오류 발생: " + cause.getMessage(), cause);

            // 복구 가능한 예외(RuntimeException)
            case 1062 -> ret = new DuplicateKeyException(
                    "중복된 기본키: " + cause.getMessage(), cause);
            case 1048 -> ret = new ColumnCannotBeNullException(
                    "Not Null 필드에 값 미할당 됨: " + cause.getMessage(), cause);
            case 1364 -> ret = new MissingDefaultValueException(
                    "NOT NULL 이고 DEFAULT 값이 없는 컬럼에 값이 미할당 됨: " + cause.getMessage(), cause);
            case 1451 -> ret = new RestrictedParentModificationException(
                    "참조하는 자식 레코드가 남아있는 부모 레코드에 대한 수정 및 삭제 거부됨: " + cause.getMessage(), cause);
            case 1452 -> ret = new ParentNotFoundException(
                    "외래키로 명시된 부모 레코드가 존재하지 않음: " + cause.getMessage(), cause);
            case 1406 -> ret = new StringTooLongException(
                    "지정된 길이보다 긴 문자열 할당 시도: " + cause.getMessage(), cause);
            case 1264 -> ret = new ValueOutOfRangeException(
                    "값 범위 초과: " + cause.getMessage(), cause);
            case 1292 -> ret = new ColumnTypeNotMatchException(
                    "컬럼 타입에 맞지 않는 값 할당 시도: " + cause.getMessage(), cause);
            case 1213 -> ret = new DeadlockException(
                    "DBMS 에서 데드락 감지 됨: " + cause.getMessage(), cause);
            case 1205 -> ret = new LockWaitingTimeoutException(
                    "락 대기 시간 초과: " + cause.getMessage(), cause);
        }

        return ret;
    }
}
