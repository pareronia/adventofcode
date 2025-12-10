package com.github.pareronia.aoc.itertools;

import java.util.Iterator;

final class Enumerate {

    private Enumerate() {}

    public static <T> Iterator<Enumerated<T>> enumerateFrom(
            final int startIndex, final Iterator<T> iterator) {

        return new Iterator<>() {
            private int i = startIndex;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Enumerated<T> next() {
                final Enumerated<T> next = new Enumerated<>(i, iterator.next());
                i++;
                return next;
            }
        };
    }
}
