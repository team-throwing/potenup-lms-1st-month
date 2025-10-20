package com.lms.repository.notice;

import com.lms.domain.course.Notice;
import com.lms.repository.exception.DatabaseException;

public interface NoticeRepository {
    Notice createNotice(Notice notice) throws DatabaseException;
}
