package com.github.pareronia.aoc.itertools;

public record Pair<T>(T first, T second) {

    public static <T> Pair<T> of(final T first, final T second) {
        return new Pair<>(first, second);
    }
}
