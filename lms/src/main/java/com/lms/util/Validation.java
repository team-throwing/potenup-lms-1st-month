package com.lms.util;

public class Validation {
    public static void requirePositive(Integer value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requirePositive(Long value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
