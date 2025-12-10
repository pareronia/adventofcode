package com.github.pareronia.aoc.itertools;

import java.util.Iterator;
import java.util.NoSuchElementException;

final class Chain {

    private Chain() {}

    public static <T> Iterator<T> chain(final Iterator<T> iterator1, final Iterator<T> iterator2) {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator1.hasNext() || iterator2.hasNext();
            }

            @Override
            public T next() {
                final T next;
                if (iterator1.hasNext()) {
                    next = iterator1.next();
                } else if (iterator2.hasNext()) {
                    next = iterator2.next();
                } else {
                    throw new NoSuchElementException();
                }
                return next;
            }
        };
    }
}
