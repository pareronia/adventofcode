package com.github.pareronia.aoc.itertools;

import java.util.Iterator;

final class Pairwise {

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
}
