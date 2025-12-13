import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2020_15 extends SolutionBase<List<Integer>, Integer, Integer> {

    private AoC2020_15(final boolean debug) {
        super(debug);
    }

    public static AoC2020_15 create() {
        return new AoC2020_15(false);
    }

    public static AoC2020_15 createDebug() {
        return new AoC2020_15(true);
    }

    @Override
    protected List<Integer> parseInput(final List<String> inputs) {
        return Stream.of(inputs.getFirst().split(",")).map(Integer::valueOf).toList();
    }

    private int play(final List<Integer> numbers, final int numberOfTurns) {
        final int[] last = new int[numberOfTurns];
        Arrays.fill(last, -1);
        for (int i = 0; i < numbers.size() - 1; i++) {
            last[numbers.get(i)] = i + 1;
        }
        int prev = numbers.getLast();
        for (int i = numbers.size(); i < numberOfTurns; i++) {
            final int prevPrev = last[prev];
            last[prev] = i;
            prev = prevPrev == -1 ? 0 : i - prevPrev;
        }
        return prev;
    }

    @Override
    public Integer solvePart1(final List<Integer> numbers) {
        return play(numbers, 2020);
    }

    @Override
    public Integer solvePart2(final List<Integer> numbers) {
        return play(numbers, 30_000_000);
    }

    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    @Samples({
        @Sample(method = "part1", input = "0,3,6", expected = "436"),
        @Sample(method = "part1", input = "1,3,2", expected = "1"),
        @Sample(method = "part1", input = "2,1,3", expected = "10"),
        @Sample(method = "part1", input = "1,2,3", expected = "27"),
        @Sample(method = "part1", input = "2,3,1", expected = "78"),
        @Sample(method = "part1", input = "3,2,1", expected = "438"),
        @Sample(method = "part1", input = "3,1,2", expected = "1836"),
        @Sample(method = "part2", input = "0,3,6", expected = "175594"),
        @Sample(method = "part2", input = "1,3,2", expected = "2578"),
        @Sample(method = "part2", input = "2,1,3", expected = "3544142"),
        @Sample(method = "part2", input = "1,2,3", expected = "261214"),
        @Sample(method = "part2", input = "2,3,1", expected = "6895259"),
        @Sample(method = "part2", input = "3,2,1", expected = "18"),
        @Sample(method = "part2", input = "3,1,2", expected = "362"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }
}
