package com.github.pareronia.aoc.itertools;

import java.util.Iterator;

final class Zip {

    private Zip() {}

    public static <T> Iterator<ZippedPair<T>> zip(
            final Iterator<T> iterator1, final Iterator<T> iterator2) {

        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return iterator1.hasNext() && iterator2.hasNext();
            }

            @Override
            public ZippedPair<T> next() {
                return new ZippedPair<>(iterator1.next(), iterator2.next());
            }
        };
    }
}
