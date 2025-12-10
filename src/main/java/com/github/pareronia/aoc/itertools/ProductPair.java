package com.github.pareronia.aoc.itertools;

public record ProductPair<T, U>(T first, U second) {
    public static <T, U> ProductPair<T, U> of(final T first, final U second) {
        return new ProductPair<>(first, second);
    }
}