package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.Range.range;
import static com.github.pareronia.aoc.Range.rangeClosed;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.IntSummaryStatistics;
import java.util.List;

import org.junit.Test;

public class RangeTestCase {

    @Test
    public void test() {
        assertThat(collect(range(8)), is(List.of(0, 1, 2, 3, 4, 5, 6, 7)));
        assertThat(collect(range(8, 8, 1)), is(List.of()));
        assertThat(collect(range(8, 8, -1)), is(List.of()));
        assertThat(collect(range(5, 6, 1)), is(List.of(5)));
        assertThat(collect(range(5, 6, 2)), is(List.of(5)));
        assertThat(collect(range(5, 7, 2)), is(List.of(5)));
        assertThat(collect(range(5, 8, 2)), is(List.of(5, 7)));
        assertThat(collect(range(1, 6, 1)), is(List.of(1, 2, 3, 4, 5)));
        assertThat(collect(range(4, 15, 2)), is(List.of(4, 6, 8, 10, 12, 14)));
        assertThat(collect(range(4, 16, 2)), is(List.of(4, 6, 8, 10, 12, 14)));
        assertThat(collect(rangeClosed(7)), is(List.of(0, 1, 2, 3, 4, 5, 6, 7)));
        assertThat(collect(rangeClosed(4, 13, 2)), is(List.of(4, 6, 8, 10, 12)));
        assertThat(collect(rangeClosed(4, 12, 2)), is(List.of(4, 6, 8, 10, 12)));
        assertThat(collect(range(7, -1, -1)), is(List.of(7, 6, 5, 4, 3, 2, 1, 0)));
        assertThat(collect(rangeClosed(3, -1, -1)), is(List.of(3, 2, 1, 0, -1)));
    }
    
    @Test
    public void testIntStream() {
        final IntSummaryStatistics stats = range(8).intStream().summaryStatistics();
        assertThat(stats.getCount(), is(8L));
        assertThat(stats.getMin(), is(0));
        assertThat(stats.getMax(), is(7));
        assertThat(stats.getSum(), is(28L));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void cannotHaveZeroStep() {
        range(10, 20, 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void cannotHaveAscendingRangeToNegative() {
        range(-10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void cannotHaveAscendingClosedRangeToNegative() {
        rangeClosed(-10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void cannotHaveNegativeStepOnAscendingRange() {
        range(10, 20, -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void cannotHavePositiveStepOnDescendingRange() {
        range(20, 10, 1);
    }
    
    private List<Integer> collect(final Range range) {
        return range.stream().collect(toList());
    }
}
