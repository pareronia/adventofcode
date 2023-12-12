import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.github.pareronia.aoc.AssertUtils;
import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_12
        extends SolutionBase<List<String>, Long, Long> {
    
    private final Map<CacheKey, Long> cache = new HashMap<>();
    
    private AoC2023_12(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_12 create() {
        return new AoC2023_12(false);
    }
    
    public static AoC2023_12 createDebug() {
        return new AoC2023_12(true);
    }
    
    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }
    
    private long count(final String s, final int[] counts, final int idx) {
        if (s.isEmpty()) {
            if (counts.length == 0 && idx == 0) {
                return 1;
            } else {
                return 0;
            }
        }
        
        final CacheKey key = new CacheKey(new String(s), counts, idx);
        if (this.cache.containsKey(key)) {
            return this.cache.get(key);
        }
        long ans = 0;
        final char[] nxts = s.charAt(0) == '?' ? new char[] {'.', '#'} : new char[] {s.charAt(0)};
        for (final char nxt : nxts) {
            if (nxt == '#') {
                ans += count(s.substring(1), counts, idx + 1);
            } else if (nxt == '.') {
                if (idx > 0) {
                    if (counts.length > 0 && counts[0] == idx) {
                        ans += count(s.substring(1), Arrays.copyOfRange(counts, 1, counts.length), 0);
                    }
                } else {
                    ans += count(s.substring(1), counts, 0);
                }
            } else {
                AssertUtils.unreachable();
            }
        }
        
        this.cache.put(key, ans);
        return ans;
    }

    @Override
    public Long solvePart1(final List<String> input) {
        long ans = 0;
        for (final String line: input) {
            this.cache.clear();
            final StringSplit split = StringOps.splitOnce(line, " ");
            final int[] counts = Arrays.stream(split.right().split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
            ans += count(split.left() + ".", counts, 0);
        }
        return ans;
    }
    
    @Override
    public Long solvePart2(final List<String> input) {
        long ans = 0;
        for (final String line: input) {
            this.cache.clear();
            final StringSplit split = StringOps.splitOnce(line, " ");
            final String s = String.join(
                "?",
                Range.range(5).intStream().mapToObj(i -> split.left()).toList()) + ".";
            final int[] counts = Range.range(5).intStream()
                .flatMap(i -> Arrays.stream(split.right().split(",")).mapToInt(Integer::parseInt))
                .toArray();
            ans += count(s, counts, 0);
        }
        return ans;
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "21"),
        @Sample(method = "part2", input = TEST, expected = "525152"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_12.createDebug().run();
    }

    private static final String TEST = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
            """;
    
    private static class CacheKey {
        private final String s;
        private final int[] counts;
        private final int idx;
        
        public CacheKey(final String s, final int[] counts, final int idx) {
            this.s = s;
            this.counts = counts;
            this.idx = idx;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(counts);
            return prime * result + Objects.hash(idx, s);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CacheKey other = (CacheKey) obj;
            return Arrays.equals(counts, other.counts)
                && idx == other.idx && Objects.equals(s, other.s);
        }
    }
}
