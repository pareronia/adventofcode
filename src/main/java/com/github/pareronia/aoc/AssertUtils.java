package com.github.pareronia.aoc;

import java.util.function.Supplier;

public class AssertUtils {

    public static void assertTrue(final boolean condition, final Supplier<String> message) {
        if (!condition) {
            throw buildException(message);
        }
    }

    public static void assertFalse(final boolean condition, final Supplier<String> message) {
        if (condition) {
            throw buildException(message);
        }
    }

    public static void assertNotNull(final Object obj, final Supplier<String> message) {
        if (obj == null) {
            throw buildException(message);
        }
    }

    private static IllegalArgumentException buildException(final Supplier<String> message) {
        return new IllegalArgumentException(message.get());
    }
}
