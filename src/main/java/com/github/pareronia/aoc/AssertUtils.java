package com.github.pareronia.aoc;

import java.util.function.Supplier;

public class AssertUtils {

    public static void assertTrue(final boolean condition, final Supplier<String> message) {
        if (!condition) {
            throw new IllegalArgumentException(message.get());
        }
    }
}
