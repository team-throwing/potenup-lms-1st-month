package com.lms.service;

import com.lms.domain.course.Notice;
import com.lms.repository.config.DataSourceFactory;
import com.lms.repository.config.RepositoryConfig;
import com.lms.repository.exception.DatabaseException;
import com.lms.repository.exception.error.DatabaseError;
import com.lms.service.dto.notice.CreateNoticeDto;
import com.lms.service.dto.notice.UpdateNoticeDto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class NoticeService {

    private final NoticeRepository noticeRepository = RepositoryConfig.noticeRepository();

    // =========================
    // 공지사항 CRUD
    // =========================
    public Notice createNotice(CreateNoticeDto dto) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            Notice notice = noticeRepository.create(dto.toEntity());
            conn.commit();

            return notice;
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("공지사항 생성 중 오류 발생", e);
        } finally {
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

    public void updateNotice(UpdateNoticeDto dto) {
        Connection conn = null;
        try {
            conn = DataSourceFactory.get().getConnection();
            conn.setAutoCommit(false);
            ConnectionHolder.set(conn);

            Notice notice = noticeRepository.findById(dto.id())
                    .orElseThrow(() -> new NoSuchElementException("공지사항을 찾을 수 없습니다."));

            // ✅ 도메인 로직을 통해 내부 상태 변경
            notice.updateBody(dto.newBody());

            noticeRepository.update(notice);
            conn.commit();
        } catch (SQLException | DatabaseException e) {
            rollbackSafely(conn);
            throw new DatabaseError("공지사항 수정 중 오류 발생", e);
        } finally {
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
            closeSafely(conn);
        }
    }

    // =========================
    // 내부 유틸
    // =========================
    private void rollbackSafely(Connection conn) {
        if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
    }

    private void closeSafely(Connection conn) {
        ConnectionHolder.clear();
        try {if (conn != null) conn.close();} catch (SQLException ignored) {}
    }
}
