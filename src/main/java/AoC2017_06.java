import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AoC2017_06 extends SolutionBase<List<Integer>, Integer, Integer> {

    private AoC2017_06(final boolean debug) {
        super(debug);
    }

    public static AoC2017_06 create() {
        return new AoC2017_06(false);
    }

    public static AoC2017_06 createDebug() {
        return new AoC2017_06(true);
    }

    @Override
    protected List<Integer> parseInput(final List<String> inputs) {
        return Arrays.stream(inputs.getFirst().split("\\s+")).map(Integer::valueOf).toList();
    }

    private Result getRedistributionCycles(final List<Integer> input) {
        final Map<List<Integer>, Integer> seen = new HashMap<>();
        List<Integer> banks = new ArrayList<>(input);
        int cnt = 0;
        while (!seen.containsKey(banks)) {
            seen.put(banks, cnt);
            final int max = banks.stream().mapToInt(Integer::valueOf).max().getAsInt();
            final int idx = banks.indexOf(max);
            final Integer blocks = banks.get(idx);
            banks = new ArrayList<>(banks);
            banks.set(idx, 0);
            for (int i = 1; i <= blocks; i++) {
                final int j = (idx + i) % banks.size();
                final int newBlocks = banks.get(j) + 1;
                banks.set(j, newBlocks);
            }
            cnt++;
        }
        return new Result(seen, banks);
    }

    @Override
    public Integer solvePart1(final List<Integer> input) {
        return getRedistributionCycles(input).map.size();
    }

    @Override
    public Integer solvePart2(final List<Integer> input) {
        final Result result = getRedistributionCycles(input);
        return result.map.size() - result.map.get(result.last);
    }

    @Samples({
        @Sample(method = "part1", input = "0 2 7 0", expected = "5"),
        @Sample(method = "part2", input = "0 2 7 0", expected = "4"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    record Result(Map<List<Integer>, Integer> map, List<Integer> last) {}
}
