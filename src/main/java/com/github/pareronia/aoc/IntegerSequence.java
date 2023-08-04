package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.AssertUtils.assertFalse;
import static com.github.pareronia.aoc.AssertUtils.assertTrue;
import static java.util.stream.Collectors.toList;

import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.ToString;

public class IntegerSequence {
    
    @ToString(onlyExplicitlyIncluded = true)
    public static class Range implements Iterable<Integer> {
        @ToString.Include
        private final int from;
        @ToString.Include
        private final int to;
        @ToString.Include
        private final int step;
        private int minimum;
        private int maximum;
        
        private Range(final int from, final int to, final int step) {
            assertTrue(step != 0, () -> "step should be != 0");
            assertFalse(from < to && step < 0, () -> "step should be > 0");
            assertFalse(from > to && step > 0, () -> "step should be < 0");
            this.from = from;
            this.to = to;
            this.step = step;
            if (from != to) {
                if (Math.abs(step) == 1) {
                    this.minimum = from < to ? from : to + 1;
                    this.maximum = from < to ? to - 1: from;
                } else {
                    //FIXME too slow
                    final List<Integer> list = this.stream().collect(toList());
                    this.minimum = from < to ? list.get(0) : Utils.last(list);
                    this.maximum = from < to ? Utils.last(list) : list.get(0);
                }
            }
        }

        public static Range range(final int to) {
            assertTrue(to > 0, () -> "to should be > 0");
            return new Range(0, to, 1);
        }
        
        public static Range rangeClosed(final int to) {
            assertTrue(to > 0, () -> "to should be > 0");
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
        
        public int getMinimum() {
            return minimum;
        }
        
        public int getMaximum() {
            return maximum;
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
}