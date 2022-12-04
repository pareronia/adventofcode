package com.github.pareronia.aoc;

import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Range implements Iterable<Integer> {
    private final int from;
    private final int to;
    private final int step;
    
    private Range(final int from, final int to, final int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step should be != 0");
        }
        if (from < to && step < 0) {
            throw new IllegalArgumentException("step should be > 0");
        }
        if (from > to && step > 0) {
            throw new IllegalArgumentException("step should be < 0");
        }
        this.from = from;
        this.to = to;
        this.step = step;
    }

    public static Range range(final int to) {
        if (to <= 0) {
            throw new IllegalArgumentException("to should be > 0");
        }
        return new Range(0, to, 1);
    }
    
    public static Range rangeClosed(final int to) {
        if (to <= 0) {
            throw new IllegalArgumentException("to should be > 0");
        }
        return new Range(0, to + 1, 1);
    }
    
    public static Range range(final int from, final int to, final int step) {
        return new Range(from, to, step);
    }
    
    public static Range rangeClosed(final int from, final int to, final int step) {
        return new Range(from, from < to ? to + 1 : to - 1, step);
    }
    
    public Stream<Integer> stream() {
        return Utils.stream(iterator());
    }
    
    public IntStream intStream() {
        final Iterator<Integer> iterator = iterator();
        return IntStream.generate(() -> 0)
                .takeWhile(x -> iterator.hasNext())
                .map(x -> iterator.next());
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<>() {
            int n = from;
            
            @Override
            public boolean hasNext() {
                if (from < to) {
                    return to > n;
                } else {
                    return to < n;
                }
            }

            @Override
            public Integer next() {
                final int next = n;
                n += step;
                return next;
            }
        };
    }
}
