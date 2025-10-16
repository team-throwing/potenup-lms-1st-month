package com.lms.repository.exception.converter;

import java.sql.SQLException;

public interface DbDialectExceptionConverter {

    Throwable convert(SQLException sqlException);
}
