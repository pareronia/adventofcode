import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_11 extends SolutionBase<List<Long>, Long, Long> {
    
    private AoC2024_11(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_11 create() {
        return new AoC2024_11(false);
    }
    
    public static AoC2024_11 createDebug() {
        return new AoC2024_11(true);
    }
    
    @Override
    protected List<Long> parseInput(final List<String> inputs) {
        return Arrays.stream(inputs.get(0).split(" "))
                .map(Long::valueOf)
                .toList();
    }
    
    private long solve(final List<Long> stones, final int blinks) {
        class Counter {
            record State(long stone, int cnt) {}
            
            static final Map<State, Long> memo = new HashMap<>();

            static long count(final long s, final int cnt) {
                final State state = new State(s, cnt);
                if (memo.containsKey(state)) {
                    return memo.get(state);
                }
                long ans;
                if (cnt == 0) {
                    ans = 1;
                } else if (s == 0) {
                    ans = count(1, cnt - 1);
                } else if (String.valueOf(s).length() % 2 == 0) {
                    final String ss = String.valueOf(s);
                    final int length = ss.length();
                    final long ss1 = Long.parseLong(ss.substring(0, length / 2));
                    final long ss2 = Long.parseLong(ss.substring(length / 2));
                    ans = count(ss1, cnt - 1) + count(ss2, cnt - 1);
                } else {
                    ans = count(s * 2024, cnt - 1);
                }
                memo.put(state, ans);
                return ans;
            }
        }
        
        return stones.stream().mapToLong(s -> Counter.count(s, blinks)).sum();
    }
    
    @Override
    public Long solvePart1(final List<Long> stones) {
        return solve(stones, 25);
    }
    
    @Override
    public Long solvePart2(final List<Long> stones) {
        return solve(stones, 75);
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "55312")
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_11.create().run();
    }

    private static final String TEST = """
            125 17
            """;
}