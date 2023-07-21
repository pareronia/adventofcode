package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.Range.between;
import static com.github.pareronia.aoc.Range.range;
import static com.github.pareronia.aoc.Range.rangeClosed;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.IntSummaryStatistics;
import java.util.List;

import org.junit.jupiter.api.Test;

public class RangeTestCase {

    @Test
    public void test() {
        assertThat(collect(range(8))).containsExactly(0, 1, 2, 3, 4, 5, 6, 7);
        assertThat(collect(range(8, 8, 1))).containsExactly();
        assertThat(collect(range(8, 8, -1))).containsExactly();
        assertThat(collect(range(5, 6, 1))).containsExactly(5);
        assertThat(collect(range(5, 6, 2))).containsExactly(5);
        assertThat(collect(range(5, 7, 2))).containsExactly(5);
        assertThat(collect(range(5, 8, 2))).containsExactly(5, 7);
        assertThat(collect(range(1, 6, 1))).containsExactly(1, 2, 3, 4, 5);
        assertThat(collect(range(4, 15, 2))).containsExactly(4, 6, 8, 10, 12, 14);
        assertThat(collect(range(4, 16, 2))).containsExactly(4, 6, 8, 10, 12, 14);
        assertThat(collect(rangeClosed(7))).containsExactly(0, 1, 2, 3, 4, 5, 6, 7);
        assertThat(collect(rangeClosed(4, 13, 2))).containsExactly(4, 6, 8, 10, 12);
        assertThat(collect(rangeClosed(4, 12, 2))).containsExactly(4, 6, 8, 10, 12);
        assertThat(collect(range(7, -1, -1))).containsExactly(7, 6, 5, 4, 3, 2, 1, 0);
        assertThat(collect(rangeClosed(3, -1, -1))).containsExactly(3, 2, 1, 0, -1);
        assertThat(collect(between(3, -1))).containsExactly(3, 2, 1, 0, -1);
        assertThat(collect(between(1, 4))).containsExactly(1, 2, 3, 4);
        assertThat(collect(between(1, 1))).isEmpty();
    }
     
    @Test
    public void contains() {
        assertThat(between(1, 4).contains(0)).isFalse();
        assertThat(between(1, 4).contains(1)).isTrue();
        assertThat(between(1, 4).contains(2)).isTrue();
        assertThat(between(1, 4).contains(3)).isTrue();
        assertThat(between(1, 4).contains(4)).isTrue();
        assertThat(between(-4, 0).contains(1)).isFalse();
        assertThat(range(4, 15, 2).contains(0)).isFalse();
        assertThat(range(4, 15, 2).contains(6)).isTrue();
        assertThat(range(4, 15, 2).contains(7)).isFalse();
        assertThat(range(14, 4, -2).contains(6)).isTrue();
        assertThat(range(14, 4, -2).contains(7)).isFalse();
        assertThat(range(1, 1, 1).contains(1)).isFalse();
        assertThat(range(1, 1, -1).contains(1)).isFalse();
        assertThat(between(1, 1).contains(1)).isFalse();
    }
    
    @Test
    public void containsRange() {
        assertThat(between(1, 4).containsRange(between(3, 5))).isFalse();
        assertThat(between(1, 4).containsRange(between(0, 4))).isFalse();
        assertThat(between(1, 4).containsRange(between(-4, 5))).isFalse();
        assertThat(between(1, 4).containsRange(between(-4, 0))).isFalse();
        assertThat(between(1, 4).containsRange(between(14, 15))).isFalse();
        assertThat(between(1, 4).containsRange(between(1, 4))).isTrue();
        assertThat(between(1, 4).containsRange(between(2, 3))).isTrue();
        assertThat(between(-5, 5).containsRange(between(1, 3))).isTrue();
        assertThat(between(1, 1).containsRange(between(0, 1))).isFalse();
        assertThat(between(1, 1).containsRange(between(1, 1))).isFalse();
        assertThat(between(2, 8).containsRange(between(3, 7))).isTrue();
//        assertThat(between(4, 6).containsRange(between(6, 6))).isTrue();
    }
    
    @Test
    public void isOverlappedBy() {
        assertThat(between(1, 4).isOverlappedBy(between(3, 5))).isTrue();
        assertThat(between(1, 4).isOverlappedBy(between(0, 4))).isTrue();
        assertThat(between(1, 4).isOverlappedBy(between(-4, 5))).isTrue();
        assertThat(between(1, 4).isOverlappedBy(between(-4, 0))).isFalse();
        assertThat(between(1, 4).isOverlappedBy(between(14, 15))).isFalse();
        assertThat(between(1, 4).isOverlappedBy(between(1, 4))).isTrue();
        assertThat(between(1, 4).isOverlappedBy(between(2, 3))).isTrue();
        assertThat(between(-5, 5).isOverlappedBy(between(1, 3))).isTrue();
        assertThat(between(1, 1).isOverlappedBy(between(0, 1))).isFalse();
        assertThat(between(1, 1).isOverlappedBy(between(1, 1))).isFalse();
    }
    
    @Test
    public void minimum_maximum() {
        assertThat(range(5).getMinimum()).isEqualTo(0);
        assertThat(range(8).getMaximum()).isEqualTo(7);
        assertThat(between(2, 5).getMinimum()).isEqualTo(2);
        assertThat(between(2, 5).getMaximum()).isEqualTo(5);
        assertThat(range(5, 2, -1).getMinimum()).isEqualTo(3);
        assertThat(range(5, 2, -1).getMaximum()).isEqualTo(5);
        assertThat(rangeClosed(5, 2, -1).getMinimum()).isEqualTo(2);
        assertThat(rangeClosed(5, 2, -1).getMaximum()).isEqualTo(5);
    }

    @Test
    public void testIntStream() {
        final IntSummaryStatistics stats = range(8).intStream().summaryStatistics();
        assertThat(stats.getCount()).isEqualTo(8L);
        assertThat(stats.getMin()).isEqualTo(0);
        assertThat(stats.getMax()).isEqualTo(7);
        assertThat(stats.getSum()).isEqualTo(28L);
    }
    
    @Test
    public void cannotHaveZeroStep() {
        assertThatThrownBy(() -> range(10, 20, 0))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void cannotHaveAscendingRangeToNegative() {
        assertThatThrownBy(() -> range(-10))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void cannotHaveAscendingClosedRangeToNegative() {
        assertThatThrownBy(() -> rangeClosed(-10))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void cannotHaveNegativeStepOnAscendingRange() {
        assertThatThrownBy(() -> range(10, 20, -1))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    public void cannotHavePositiveStepOnDescendingRange() {
        assertThatThrownBy(() -> range(20, 10, 1))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    private List<Integer> collect(final Range range) {
        return range.stream().collect(toList());
    }
}
