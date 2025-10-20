package com.lms.repository.notice;

import com.lms.domain.course.Notice;
import com.lms.repository.exception.DatabaseException;
import com.lms.repository.exception.converter.DbUtils;
import com.lms.service.ConnectionHolder;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoticeRepositoryImpl implements NoticeRepository {
    private final DbUtils dbUtils;

    public NoticeRepositoryImpl(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    @Override
    public void createNotice(Notice notice) throws DatabaseException {
        String sql = """
            INSERT INTO notice (id, body, course_id, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?)
            """;

        Connection conn = ConnectionHolder.get();

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (notice.getId() == null) {
                pstmt.setNull(1, Types.BIGINT);
            } else {
                pstmt.setLong(1, notice.getId());
            }
            pstmt.setString(2,  notice.getBody());
            pstmt.setInt(3,  notice.getCourseId());
            pstmt.setObject(4, notice.getCreatedAt());
            pstmt.setObject(5, notice.getUpdatedAt());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }
    }

    @Override
    public void updateNotice(Notice notice) throws DatabaseException {
        String sql = """
            UPDATE notice SET
            body = ?, course_id = ?, updated_at = ?
            WHERE id = ?
            """;

        Connection conn = ConnectionHolder.get();

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, notice.getBody());
            pstmt.setInt(2, notice.getCourseId());
            pstmt.setObject(3, notice.getUpdatedAt());
            pstmt.setLong(4, notice.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }
    }

    @Override
    public void deleteNotice(Notice notice) throws DatabaseException {
        String sql = """
            DELETE FROM notice WHERE id = ?
            """;

        Connection conn = ConnectionHolder.get();

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, notice.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }
    }

    @Override
    public Notice findById(Long noticeId) {
        String sql = """
            SELECT id, body, course_id, created_at, updated_at
            FROM notice WHERE id = ?
            """;

        Connection conn = ConnectionHolder.get();

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, noticeId);

            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Notice.rebuild(
                        rs.getLong("id"),
                        rs.getString("body"),
                        rs.getInt("course_id"),
                        rs.getObject("created_at", LocalDateTime.class),
                        rs.getObject("updated_at", LocalDateTime.class)
                    );
                }
            }
        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        return null;
    }

    @Override
    public List<Notice> findAllNoticeByCourseId(Integer courseId) {
        String sql = """
            SELECT id, body, course_id, created_at, updated_at
            FROM notice WHERE course_id = ?
            """;
        Connection conn = ConnectionHolder.get();

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, courseId);

            ResultSet rs = pstmt.executeQuery();

            List<Notice> noticeList = new ArrayList<>();
            while (rs.next()) {
                noticeList.add(
                    Notice.rebuild(
                        rs.getLong("id"),
                        rs.getString("body"),
                        rs.getInt("course_id"),
                        LocalDateTime.parse(rs.getString("created_at")),
                        LocalDateTime.parse(rs.getString("updated_at"))
                    )
                );
            }

            return noticeList;

        } catch (SQLException e) {
            dbUtils.handleSQLException(conn, e);
        }

        return Collections.emptyList();
    }
}
