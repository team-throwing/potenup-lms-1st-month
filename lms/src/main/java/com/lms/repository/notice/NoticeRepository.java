package com.lms.repository.notice;

import com.lms.domain.course.Notice;
import com.lms.repository.exception.DatabaseException;

import java.util.List;

public interface NoticeRepository {
    void createNotice(Notice notice) throws DatabaseException;

    void updateNotice(Notice notice) throws DatabaseException;

    void deleteNotice(Notice notice) throws DatabaseException;

    Notice findById(Long noticeId);

    List<Notice> findAllNoticeByCourseId(Integer courseId);
}
