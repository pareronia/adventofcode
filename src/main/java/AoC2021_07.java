import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2021_07 extends SolutionBase<List<Integer>, Integer, Integer> {
    
    private AoC2021_07(final boolean debug) {
        super(debug);
    }

    public static final AoC2021_07 create() {
        return new AoC2021_07(false);
    }

    public static final AoC2021_07 createDebug() {
        return new AoC2021_07(true);
    }

    @Override
    protected List<Integer> parseInput(final List<String> inputs) {
        return Arrays.stream(inputs.get(0).split(","))
                .map(Integer::valueOf)
                .toList();
    }

    private int solve(
            final List<Integer> positions,
            final IntBinaryOperator calc
    ) {
        final IntSummaryStatistics summary = positions.stream()
                .mapToInt(Integer::intValue).summaryStatistics();
        return IntStream.rangeClosed(summary.getMin(), summary.getMax())
                .map(a -> positions.stream()
                            .mapToInt(Integer::intValue)
                            .map(b -> calc.applyAsInt(a, b))
                            .sum())
                .min().orElseThrow();
    }
    
    @Override
    public Integer solvePart1(final List<Integer> positions) {
        return solve(positions, (a, b) -> Math.abs(a - b));
    }

    @Override
    public Integer solvePart2(final List<Integer> positions) {
        return solve(positions, (a, b) -> {
            final int diff = Math.abs(a - b);
            return (diff * (diff + 1)) / 2;
        });
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "37"),
        @Sample(method = "part2", input = TEST, expected = "168"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2021_07.create().run();
    }

    private static final String TEST = "16,1,2,0,4,2,7,1,2,14";
}
