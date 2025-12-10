package com.github.pareronia.aoc.itertools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class Cycle {

    private Cycle() {}

    public static <T> Iterator<T> cycle(final Iterator<T> iterator) {
        return new Iterator<>() {
            final List<T> saved = new ArrayList<>();
            int i;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                final T next;
                if (iterator.hasNext()) {
                    next = iterator.next();
                    saved.add(next);
                } else {
                    next = saved.get(i % saved.size());
                    i++;
                }
                return next;
            }
        };
    }
}
