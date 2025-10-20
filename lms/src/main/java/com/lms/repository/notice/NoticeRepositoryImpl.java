package com.lms.repository.notice;

import com.lms.domain.course.Notice;
import com.lms.repository.exception.DatabaseException;

public class NoticeRepositoryImpl {
    Notice createNotice(Notice notice) throws DatabaseException;
}
