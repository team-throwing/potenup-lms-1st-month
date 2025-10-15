package com.lms.service;

import java.sql.Connection;

public class ConnectionHolder {

    private static final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    public static Connection get() {
        return connectionThreadLocal.get();
    }

    public static void set(Connection connection) {
        connectionThreadLocal.set(connection);
    }

    public static void clear() {
        connectionThreadLocal.remove();
    }
}