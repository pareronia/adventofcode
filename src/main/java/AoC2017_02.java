import static com.github.pareronia.aoc.IterTools.combinations;

import com.github.pareronia.aoc.AssertUtils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.ToIntFunction;

public final class AoC2017_02 extends SolutionBase<List<List<Integer>>, Integer, Integer> {

    private AoC2017_02(final boolean debug) {
        super(debug);
    }

    public static AoC2017_02 create() {
        return new AoC2017_02(false);
    }

    public static AoC2017_02 createDebug() {
        return new AoC2017_02(true);
    }

    @Override
    protected List<List<Integer>> parseInput(final List<String> inputs) {
        return inputs.stream()
                .map(s -> Arrays.stream(s.split("\\s+")).map(Integer::valueOf).toList())
                .toList();
    }

    private int differenceHighestLowest(final List<Integer> numbers) {
        final IntSummaryStatistics stats =
                numbers.stream().mapToInt(Integer::intValue).summaryStatistics();
        return stats.getMax() - stats.getMin();
    }

    private int evenlyDivisibleQuotient(final List<Integer> numbers) {
        for (final int[] c : combinations(numbers.size(), 2).iterable()) {
            final int n1 = numbers.get(c[0]);
            final int n2 = numbers.get(c[1]);
            if (n1 > n2) {
                if (n1 % n2 == 0) {
                    return n1 / n2;
                }
            } else if (n2 % n1 == 0) {
                return n2 / n1;
            }
        }
        throw AssertUtils.unreachable();
    }

    private int sum(final List<List<Integer>> input, final ToIntFunction<List<Integer>> mapper) {
        return input.stream().mapToInt(mapper).sum();
    }

    @Override
    public Integer solvePart1(final List<List<Integer>> input) {
        return sum(input, this::differenceHighestLowest);
    }

    @Override
    public Integer solvePart2(final List<List<Integer>> input) {
        return sum(input, this::evenlyDivisibleQuotient);
    }

    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "18"),
        @Sample(method = "part2", input = TEST2, expected = "9"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST1 =
            """
            5 1 9 5
            7 5 3
            2 4 6 8
            """;
    private static final String TEST2 =
            """
            5 9 2 8
            9 4 7 3
            3 8 6 5
            """;
}
