package com.github.pareronia.aoc;

import java.util.Iterator;

public final class Pairwise {

    private Pairwise() {}

    public static <T> Iterator<Pair<T>> pairwise(final Iterator<T> iterator) {
        return new Iterator<>() {
            private T first = iterator.next();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Pair<T> next() {
                final T second = iterator.next();
                final Pair<T> pair = Pair.of(first, second);
                first = second;
                return pair;
            }
        };
    }

    public record Pair<T>(T first, T second) {
        @SuppressWarnings("PMD.ShortMethodName")
        public static <T> Pair<T> of(final T first, final T second) {
            return new Pair<>(first, second);
        }
    }
}
