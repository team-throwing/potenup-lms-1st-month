package com.lms.repository.config;

import com.lms.repository.asset.AssetRepository;
import com.lms.repository.asset.AssetRepositoryImpl;
import com.lms.repository.category.CategoryRepository;
import com.lms.repository.category.CategoryRepositoryImpl;
import com.lms.repository.course.*;
import com.lms.repository.exception.converter.DbDialectExceptionConverter;
import com.lms.repository.exception.converter.DbUtils;
import com.lms.repository.exception.converter.MysqlExceptionConverter;
import com.lms.repository.notice.NoticeRepository;
import com.lms.repository.notice.NoticeRepositoryImpl;

public class RepositoryConfig {

    private static DbUtils dbUtils;
    private static DbDialectExceptionConverter dbDialectExceptionConverter;
    private static CategoryRepository categoryRepository;
    private static CourseRepository courseRepository;
    private static SectionRepository sectionRepository;
    private static ContentRepository contentRepository;
    private static NoticeRepository noticeRepository;
    private static AssetRepository assetRepository;

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

    public static CourseRepository courseRepository() {
        if (courseRepository == null) {
            courseRepository = new CourseRepositoryImpl(dbUtils(), sectionRepository());
        }

        return courseRepository;
    }

    public static SectionRepository sectionRepository() {
        if (sectionRepository == null) {
            sectionRepository = new SectionRepositoryImpl(dbUtils(), contentRepository());
        }

        return sectionRepository;
    }

    public static ContentRepository contentRepository() {
        if (contentRepository == null) {
            contentRepository = new ContentRepositoryImpl(dbUtils());
        }

        return contentRepository;
    }

    public static NoticeRepository noticeRepository() {
        if (noticeRepository == null) {
            noticeRepository = new NoticeRepositoryImpl(dbUtils());
        }

        return noticeRepository;
    }

    public static AssetRepository assetRepository() {
        if (assetRepository == null) {
            assetRepository = new AssetRepositoryImpl(dbUtils());
        }

        return assetRepository;
    }
}
