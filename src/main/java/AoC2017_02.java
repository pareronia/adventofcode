import static com.github.pareronia.aoc.IterTools.combinations;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.ToIntFunction;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_02 extends AoCBase {

    private final transient List<List<Integer>> input;
    
    private AoC2017_02(final List<String> inputs, final boolean debug) {
        super(debug);
        this.input = inputs.stream()
                .map(s -> Arrays.stream(s.split("\\s+"))
                            .map(Integer::valueOf).collect(toList()))
                .collect(toList());
        log(this.input);
    }

    public static AoC2017_02 create(final List<String> input) {
        return new AoC2017_02(input, false);
    }

    public static AoC2017_02 createDebug(final List<String> input) {
        return new AoC2017_02(input, true);
    }
    
    private int differenceHighestLowest(final List<Integer> numbers) {
        final IntSummaryStatistics stats = numbers.stream()
                .mapToInt(Integer::intValue)
                .summaryStatistics();
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
        throw new IllegalStateException("Unsolvable");
    }
    
    private int sum(final ToIntFunction<List<Integer>> mapper) {
        return this.input.stream().mapToInt(mapper).sum();
    }

    @Override
    public Integer solvePart1() {
        return sum(this::differenceHighestLowest);
    }
    
    @Override
    public Integer solvePart2() {
        return sum(this::evenlyDivisibleQuotient);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_02.createDebug(TEST1).solvePart1() == 18;
        assert AoC2017_02.createDebug(TEST2).solvePart2() == 9;

        final Puzzle puzzle = Aocd.puzzle(2017, 2);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2017_02.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2017_02.create(inputData)::solvePart2)
        );
    }
    
    private static final List<String> TEST1 = splitLines(
            """
                5 1 9 5
                7 5 3
                2 4 6 8"""
    );
    private static final List<String> TEST2 = splitLines(
            """
                5 9 2 8
                9 4 7 3
                3 8 6 5"""
    );
}
