package com.github.pareronia.aoc;

import static com.github.pareronia.aoc.RangeInclusive.between;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class RangeInclusiveTestCase {
    
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

   @Test
   public void testRangeMergerDisjoint() {
       final Set<RangeInclusive<Integer>> ranges = Set.of(
           RangeInclusive.between(1, 6),
           RangeInclusive.between(8, 11)
       );
       
       final List<RangeInclusive<Integer>> result = RangeInclusive.mergeRanges(ranges);
       
       assertThat(result).containsExactly(RangeInclusive.between(1, 6), RangeInclusive.between(8, 11));
   }

   @Test
   public void testRangeMergerOverlap() {
       final Set<RangeInclusive<Integer>> ranges = Set.of(
               RangeInclusive.between(1, 6),
               RangeInclusive.between(5, 11)
               );
       
       final List<RangeInclusive<Integer>> result = RangeInclusive.mergeRanges(ranges);
       
       assertThat(result).containsExactly(RangeInclusive.between(1, 11));
   }

   @Test
   public void testRangeMergerDisjointAndOverlap() {
       final Set<RangeInclusive<Integer>> ranges = Set.of(
               RangeInclusive.between(5, 11),
               RangeInclusive.between(8, 11),
               RangeInclusive.between(1, 6),
               RangeInclusive.between(15, 21)
               );
       
       final List<RangeInclusive<Integer>> result = RangeInclusive.mergeRanges(ranges);
       
       assertThat(result).containsExactly(RangeInclusive.between(1, 11), RangeInclusive.between(15, 21));
   }
   
   @Test
   public void mergeSample() {
       // [[-2..2], [12..12], [2..14], [14..18], [16..24], [2..2]]
       final Set<RangeInclusive<Integer>> ranges = Set.of(
               RangeInclusive.between(-2, 2),
               RangeInclusive.between(2, 14),
               RangeInclusive.between(14, 18),
               RangeInclusive.between(16, 24),
               RangeInclusive.between(2, 2),
               RangeInclusive.between(12, 12)
               );
       
       final List<RangeInclusive<Integer>> result = RangeInclusive.mergeRanges(ranges);
       
       assertThat(result).containsExactly(RangeInclusive.between(-2, 24));
   }
}
