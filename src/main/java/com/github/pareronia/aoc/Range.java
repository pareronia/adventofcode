package com.github.pareronia.aoc;

import static java.util.stream.Collectors.toList;

import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.ToString;

@ToString(onlyExplicitlyIncluded = true)
public class Range implements Iterable<Integer> {
    @ToString.Include
    private final int from;
    @ToString.Include
    private final int to;
    @ToString.Include
    private final int step;
    private int minimum;
    private int maximum;
    private boolean empty;
    
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
        final List<Integer> list = this.stream().collect(toList());
        if (from != to) {
            this.minimum = from < to ? list.get(0) : Utils.last(list);
            this.maximum = from < to ? Utils.last(list) : list.get(0);
        } else {
            this.empty = true;
        }
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
        return new Range(from, from == to ? to : (from < to ? to + 1 : to - 1), step);
    }
    
    public static Range between(final int fromInclusive, final int toInclusive) {
        final int step = fromInclusive > toInclusive ? -1 : 1;
        return Range.rangeClosed(fromInclusive, toInclusive, step);
    }
    
    public boolean contains(final int value) {
        return !empty && minimum <= value && value <= maximum && ((value - minimum) % step) == 0;
    }
    
    public boolean containsRange(final Range other) {
        if (other == null || empty || other.empty) {
            return false;
        }
        if (step != 1 || other.step != 1) {
            throw new UnsupportedOperationException("only available for step=1");
        }
        return contains(other.minimum) && contains(other.maximum);
    }
   
    public boolean isOverlappedBy(final Range other) {
        if (other == null || empty || other.empty) {
            return false;
        }
        if (step != 1 || other.step != 1) {
            throw new UnsupportedOperationException("only available for step=1");
        }
        return other.contains(minimum) || other.contains(maximum) || contains(other.minimum);
    }
    
    public int getMinimum() {
        return minimum;
    }
    
    public int getMaximum() {
        return maximum;
    }
    
    public boolean isBefore(final int n) {
        return maximum < n;
    }
    
    public boolean isAfter(final int n) {
        return n < minimum;
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
