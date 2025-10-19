package com.lms;

import com.lms.repository.category.CategoryRepository;
import com.lms.repository.category.CategoryRepositoryImpl;
import com.lms.repository.exception.converter.DbDialectExceptionConverter;
import com.lms.repository.exception.converter.DbUtils;
import com.lms.repository.exception.converter.MysqlExceptionConverter;
import com.lms.service.CategoryService;
import com.lms.view.*;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        DbDialectExceptionConverter exceptionConverter = new MysqlExceptionConverter();
        DbUtils dbUtils = new DbUtils(exceptionConverter);
        CategoryRepository categoryRepository = new CategoryRepositoryImpl(dbUtils);
//        CourseRepository courseRepository = new CourseRepositoryImpl(dbUtils);
        CategoryService categoryService = new CategoryService(categoryRepository);
//        CourseService courseService = new CourseService(courseRepository);
        CategoryManagement categoryManagement = new CategoryManagement(categoryService);
//        CourseManagement courseManagement = new CourseManagement(courseService);
        CategoryMenu categoryMenu = new CategoryMenu(categoryManagement);
//        CourseManu courseManu = new CourseManu(courseManagement);
        SearchMenu searchMenu = new SearchMenu(categoryManagement); // courseManagement 추가해야함
        MainMenu mainMenu = new MainMenu(categoryMenu, searchMenu);

        mainMenu.showMainMenu(sc);
    }
}
