import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aoc.AssertUtils;
import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_12
        extends SolutionBase<List<String>, Long, Long> {
    
    private static final char[] CHARS = {'.', '#'};
    private final Map<Integer, Long> cache = new HashMap<>();
    
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
    
    private long count(
            final String s,
            final int[] counts,
            final int s_idx,
            final int c_idx,
            final int idx
    ) {
        if (s_idx == s.length()) {
            if (c_idx == counts.length && idx == 0) {
                return 1;
            } else {
                return 0;
            }
        }
        if (idx == 0) {
            int sum = 0;
            for (int i = c_idx; i < counts.length; i++) {
                sum += counts[i];
            }
            if (s.length() - s_idx < sum + counts.length - c_idx - 1) {
                return 0;
            }
        }
        
        final int key = s_idx << 16 | c_idx << 8 | idx;
        if (this.cache.containsKey(key)) {
            return this.cache.get(key);
        }
        long ans = 0;
        final char[] nxts = s.charAt(s_idx) == '?' ? CHARS : new char[] { s.charAt(s_idx) };
        for (final char nxt : nxts) {
            if (nxt == '#') {
                ans += count(s, counts, s_idx + 1, c_idx, idx + 1);
            } else if (nxt == '.') {
                if (idx > 0) {
                    if (c_idx < counts.length && counts[c_idx] == idx) {
                        ans += count(s, counts, s_idx + 1, c_idx + 1, 0);
                    }
                } else {
                    ans += count(s, counts, s_idx + 1, c_idx, 0);
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
            ans += count(split.left() + ".", counts, 0, 0, 0);
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
            ans += count(s, counts, 0, 0, 0);
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
}
