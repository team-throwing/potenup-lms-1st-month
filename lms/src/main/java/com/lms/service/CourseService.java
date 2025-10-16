package com.lms.service;

import com.lms.domain.course.Course;
import com.lms.repository.course.CourseRepository;
import com.lms.repository.course.dto.CourseInfo;

import java.sql.Connection;
import java.sql.SQLException;

public class CourseService {

    private final CourseRepository courseRepository;
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }


    public void createCourse (CourseInfo dto) throws SQLException {
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            ConnectionHolder.set(conn);
            conn.setAutoCommit(false);

            if (dto.title() == null || dto.title().isEmpty()) {
                throw new IllegalArgumentException("제목 누락");
            }


            courseRepository.create(toEntity(dto));

            conn.commit();
        } catch (Exception e) {
                throw new RuntimeException(e);
        }

    }

    public void updateCourse (CourseInfo dto) throws SQLException {
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            ConnectionHolder.set(conn);
            conn.setAutoCommit(false);
            if (dto.title() == null || dto.title().isEmpty()) {
                throw new IllegalArgumentException("수정할 뭐시기 없음?");
            }

            courseRepository.update(toEntity(dto));
            conn.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCourse (CourseInfo dto) throws SQLException {
        Connection conn = null;
        try {
            conn = DataSource.getConnection();
            ConnectionHolder.set(conn);
            conn.setAutoCommit(false);
            if (dto.title() != null || !dto.title().isEmpty()) {
                throw new IllegalArgumentException("삭제할게없음");
            }

            courseRepository.delete(toEntity(dto));
        }
    }




        private Course toEntity(CourseInfo dto) {
            return new Course(
                    dto.id(),
                    dto.title(),
                    dto.summary(),
                    dto.detail(),
                    dto.subCategoryId(),
                    dto.createdAt(),
                    dto.updatedAt(),
                    dto.userId();}

}
