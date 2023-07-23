package com.github.pareronia.aoc;

public class AssertUtils {

    public static void assertTrue(final boolean condition, final String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
