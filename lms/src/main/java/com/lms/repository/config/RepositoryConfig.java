package com.lms.repository.config;

import com.lms.repository.category.CategoryRepository;
import com.lms.repository.category.CategoryRepositoryImpl;
import com.lms.repository.exception.converter.DbDialectExceptionConverter;
import com.lms.repository.exception.converter.DbUtils;
import com.lms.repository.exception.converter.MysqlExceptionConverter;

public class RepositoryConfig {

    private static DbUtils dbUtils;
    private static DbDialectExceptionConverter dbDialectExceptionConverter;
    private static CategoryRepository categoryRepository;

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

    public static CategoryRepository categoryRepository() {
        if (categoryRepository == null) {
            categoryRepository = new CategoryRepositoryImpl(dbUtils());
        }

        return categoryRepository;
    }
}
