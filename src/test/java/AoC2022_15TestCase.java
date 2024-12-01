import static org.assertj.core.api.Assertions.assertThat;

import java.util.Deque;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.github.pareronia.aoc.RangeInclusive;

public class AoC2022_15TestCase {

    @Test
    public void testRangeMergerDisjoint() {
        final Set<RangeInclusive<Integer>> ranges = Set.of(
            RangeInclusive.between(1, 6),
            RangeInclusive.between(8, 11)
        );
        
        final Deque<RangeInclusive<Integer>> result = AoC2022_15.RangeMerger.mergeRanges(ranges);
        
        assertThat(result).containsExactly(RangeInclusive.between(1, 6), RangeInclusive.between(8, 11));
    }

    @Test
    public void testRangeMergerOverlap() {
        final Set<RangeInclusive<Integer>> ranges = Set.of(
                RangeInclusive.between(1, 6),
                RangeInclusive.between(5, 11)
                );
        
        final Deque<RangeInclusive<Integer>> result = AoC2022_15.RangeMerger.mergeRanges(ranges);
        
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
        
        final Deque<RangeInclusive<Integer>> result = AoC2022_15.RangeMerger.mergeRanges(ranges);
        
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
        
        final Deque<RangeInclusive<Integer>> result = AoC2022_15.RangeMerger.mergeRanges(ranges);
        
        assertThat(result).containsExactly(RangeInclusive.between(-2, 24));
    }
}
