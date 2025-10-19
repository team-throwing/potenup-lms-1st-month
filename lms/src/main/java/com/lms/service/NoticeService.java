package com.lms.service;

import com.lms.domain.course.Notice;
import com.lms.repository.config.DataSourceFactory;
import com.lms.repository.config.RepositoryConfig;
import com.lms.repository.exception.DatabaseException;
import com.lms.repository.exception.error.DatabaseError;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class NoticeService {

    private final NoticeRepository noticeRepository = RepositoryConfig.noticeRepository();

    // =========================
    // 공지사항 CRUD
    // =========================
    public Notice createNotice(String body, Integer courseId) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            Notice notice = Notice.create(body, courseId);
            noticeRepository.create(notice);
            conn.commit();
            return notice;
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("공지사항 생성 중 오류 발생", e);
        } finally {
            clearConnection();
            closeSafely(conn);
        }
    }

    public Notice findNoticeById(Long id) {
        try {
            return noticeRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("공지사항을 찾을 수 없습니다."));
        } catch (DatabaseException e) {
            throw new DatabaseError("공지사항 조회 중 오류 발생", e);
        }
    }

    public void updateNotice(Long id, String newBody, Integer courseId,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            Notice notice = noticeRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("공지사항을 찾을 수 없습니다."));
            notice = notice.rebuild(id, newBody, courseId, createdAt, updatedAt);
            noticeRepository.update(notice);
            conn.commit();
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("공지사항 수정 중 오류 발생", e);
        } finally {
            clearConnection();
            closeSafely(conn);
        }
    }

    public void deleteNotice(Long id) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            noticeRepository.delete(id);
            conn.commit();
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("공지사항 삭제 중 오류 발생", e);
        } finally {
            clearConnection();
            closeSafely(conn);
        }
    }

    // =========================
    // 내부 유틸
    // =========================
    private void rollbackSafely(Connection conn) {
        if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
    }

    private void clearConnection() {
        try { ConnectionHolder.clear(); } catch (Exception ignored) {}
    }

    private void closeSafely(Connection conn) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
    }
}
