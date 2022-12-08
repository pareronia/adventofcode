package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.Range.between;
import static com.github.pareronia.aoc.Range.range;
import static com.github.pareronia.aoc.Range.rangeClosed;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
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
        assertThat(collect(between(3, -1)), is(List.of(3, 2, 1, 0, -1)));
        assertThat(collect(between(1, 4)), is(List.of(1, 2, 3, 4)));
        assertThat(collect(between(1, 1)), is(List.of()));
    }
     
    @Test
    public void contains() {
        assertThat(between(1, 4).contains(0), is(FALSE));
        assertThat(between(1, 4).contains(1), is(TRUE));
        assertThat(between(1, 4).contains(2), is(TRUE));
        assertThat(between(1, 4).contains(3), is(TRUE));
        assertThat(between(1, 4).contains(4), is(TRUE));
        assertThat(between(-4, 0).contains(1), is(FALSE));
        assertThat(range(4, 15, 2).contains(0), is(FALSE));
        assertThat(range(4, 15, 2).contains(6), is(TRUE));
        assertThat(range(4, 15, 2).contains(7), is(FALSE));
        assertThat(range(14, 4, -2).contains(6), is(TRUE));
        assertThat(range(14, 4, -2).contains(7), is(FALSE));
        assertThat(range(1, 1, 1).contains(1), is(FALSE));
        assertThat(range(1, 1, -1).contains(1), is(FALSE));
        assertThat(between(1, 1).contains(1), is(FALSE));
    }
    
    @Test
    public void containsRange() {
        assertThat(between(1, 4).containsRange(between(3, 5)), is(FALSE));
        assertThat(between(1, 4).containsRange(between(0, 4)), is(FALSE));
        assertThat(between(1, 4).containsRange(between(-4, 5)), is(FALSE));
        assertThat(between(1, 4).containsRange(between(-4, 0)), is(FALSE));
        assertThat(between(1, 4).containsRange(between(14, 15)), is(FALSE));
        assertThat(between(1, 4).containsRange(between(1, 4)), is(TRUE));
        assertThat(between(1, 4).containsRange(between(2, 3)), is(TRUE));
        assertThat(between(-5, 5).containsRange(between(1, 3)), is(TRUE));
        assertThat(between(1, 1).containsRange(between(0, 1)), is(FALSE));
        assertThat(between(1, 1).containsRange(between(1, 1)), is(FALSE));
        assertThat(between(2, 8).containsRange(between(3, 7)), is(TRUE));
//        assertThat(between(4, 6).containsRange(between(6, 6)), is(TRUE));
    }
    
    @Test
    public void isOverlappedBy() {
        assertThat(between(1, 4).isOverlappedBy(between(3, 5)), is(TRUE));
        assertThat(between(1, 4).isOverlappedBy(between(0, 4)), is(TRUE));
        assertThat(between(1, 4).isOverlappedBy(between(-4, 5)), is(TRUE));
        assertThat(between(1, 4).isOverlappedBy(between(-4, 0)), is(FALSE));
        assertThat(between(1, 4).isOverlappedBy(between(14, 15)), is(FALSE));
        assertThat(between(1, 4).isOverlappedBy(between(1, 4)), is(TRUE));
        assertThat(between(1, 4).isOverlappedBy(between(2, 3)), is(TRUE));
        assertThat(between(-5, 5).isOverlappedBy(between(1, 3)), is(TRUE));
        assertThat(between(1, 1).isOverlappedBy(between(0, 1)), is(FALSE));
        assertThat(between(1, 1).isOverlappedBy(between(1, 1)), is(FALSE));
    }
    
    @Test
    public void minimum_maximum() {
        assertThat(range(5).getMinimum(), is(0));
        assertThat(range(8).getMaximum(), is(7));
        assertThat(between(2, 5).getMinimum(), is(2));
        assertThat(between(2, 5).getMaximum(), is(5));
        assertThat(range(5, 2, -1).getMinimum(), is(3));
        assertThat(range(5, 2, -1).getMaximum(), is(5));
        assertThat(rangeClosed(5, 2, -1).getMinimum(), is(2));
        assertThat(rangeClosed(5, 2, -1).getMaximum(), is(5));
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
