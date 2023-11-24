package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.Range.between;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RangeTestCase {

    
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
        assertThat(between(1, 1).isOverlappedBy(between(0, 1))).isTrue();
        assertThat(between(1, 1).isOverlappedBy(between(1, 1))).isTrue();
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
        assertThat(between(1, 1).containsRange(between(1, 1))).isTrue();
        assertThat(between(2, 8).containsRange(between(3, 7))).isTrue();
        assertThat(between(4, 6).containsRange(between(6, 6))).isTrue();
    }
    
   @Test
   public void contains() {
       assertThat(between(1, 4).contains(0)).isFalse();
       assertThat(between(1, 4).contains(1)).isTrue();
       assertThat(between(1, 4).contains(2)).isTrue();
       assertThat(between(1, 4).contains(3)).isTrue();
       assertThat(between(1, 4).contains(4)).isTrue();
       assertThat(between(-4, 0).contains(1)).isFalse();
       assertThat(between(1, 1).contains(1)).isTrue();
   }
}
