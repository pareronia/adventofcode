import static org.assertj.core.api.Assertions.assertThat;

import java.util.Deque;
import java.util.Set;

import org.apache.commons.lang3.Range;
import org.junit.jupiter.api.Test;

public class AoC2022_15TestCase {

    @Test
    public void testRangeMergerDisjoint() {
        final Set<Range<Integer>> ranges = Set.of(
            Range.between(1, 6),
            Range.between(8, 11)
        );
        
        final Deque<Range<Integer>> result = AoC2022_15.RangeMerger.mergeRanges(ranges);
        
        assertThat(result).containsExactly(Range.between(1, 6), Range.between(8, 11));
    }

    @Test
    public void testRangeMergerOverlap() {
        final Set<Range<Integer>> ranges = Set.of(
                Range.between(1, 6),
                Range.between(5, 11)
                );
        
        final Deque<Range<Integer>> result = AoC2022_15.RangeMerger.mergeRanges(ranges);
        
        assertThat(result).containsExactly(Range.between(1, 11));
    }

    @Test
    public void testRangeMergerDisjointAndOverlap() {
        final Set<Range<Integer>> ranges = Set.of(
                Range.between(5, 11),
                Range.between(8, 11),
                Range.between(1, 6),
                Range.between(15, 21)
                );
        
        final Deque<Range<Integer>> result = AoC2022_15.RangeMerger.mergeRanges(ranges);
        
        assertThat(result).containsExactly(Range.between(1, 11), Range.between(15, 21));
    }
    
    @Test
    public void mergeSample() {
        // [[-2..2], [12..12], [2..14], [14..18], [16..24], [2..2]]
        final Set<Range<Integer>> ranges = Set.of(
                Range.between(-2, 2),
                Range.between(2, 14),
                Range.between(14, 18),
                Range.between(16, 24),
                Range.between(2, 2),
                Range.between(12, 12)
                );
        
        final Deque<Range<Integer>> result = AoC2022_15.RangeMerger.mergeRanges(ranges);
        
        assertThat(result).containsExactly(Range.between(-2, 24));
    }
}
