package com.lms.repository.config;

import com.lms.repository.exception.converter.DbDialectExceptionConverter;
import com.lms.repository.exception.converter.DbUtils;
import com.lms.repository.exception.converter.MysqlExceptionConverter;

public class RepositoryConfig {

    private static DbUtils dbUtils;
    private static DbDialectExceptionConverter dbDialectExceptionConverter;

    public static DbDialectExceptionConverter dbDialectExceptionConverter() {
        if (dbDialectExceptionConverter == null) {
            dbDialectExceptionConverter = new MysqlExceptionConverter();
        }

        return dbDialectExceptionConverter;
    }

    public static DbUtils dbUtils() {
        if (dbUtils == null) {
            dbUtils = new DbUtils(dbDialectExceptionConverter());
        }

        return dbUtils;
    }
}
