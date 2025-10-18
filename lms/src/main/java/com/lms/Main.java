package com.lms;

import com.lms.repository.category.CategoryRepository;
import com.lms.repository.category.CategoryRepositoryImpl;
import com.lms.repository.exception.converter.DbDialectExceptionConverter;
import com.lms.repository.exception.converter.DbUtils;
import com.lms.repository.exception.converter.MysqlExceptionConverter;
import com.lms.service.CategoryService;
import com.lms.view.CategoryManagement;
import com.lms.view.MainMenu;
import com.lms.view.CategoryManu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DbDialectExceptionConverter exceptionConverter = new MysqlExceptionConverter();
        DbUtils dbUtils = new DbUtils(exceptionConverter);
        CategoryRepository categoryRepository = new CategoryRepositoryImpl(dbUtils);
        CategoryService categoryService = new CategoryService(categoryRepository);
        CategoryManagement categoryManagement = new CategoryManagement(categoryService);
        CategoryManu categoryManu = new CategoryManu(categoryManagement);
//        SearchMenu searchMenu = new SearchMenu(categoryService, courseService);
        MainMenu mainMenu = new MainMenu(categoryManu);

        mainMenu.showMainMenu(sc);
    }
}
