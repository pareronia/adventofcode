import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.BinaryOperator;

import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_01 extends AoCBase {
    
    private final List<List<Integer>> calories;
    
    private AoC2022_01(final List<String> input, final boolean debug) {
        super(debug);
        this.calories = toBlocks(input).stream()
            .map(block -> block.stream().map(Integer::valueOf).collect(toList()))
            .collect(toList());
    }
    
    public static final AoC2022_01 create(final List<String> input) {
        return new AoC2022_01(input, false);
    }

    public static final AoC2022_01 createDebug(final List<String> input) {
        return new AoC2022_01(input, true);
    }
    
    private int solve(final int count) {
        final BinaryOperator<Integer> sum = (a, b) -> a + b;
        return this.calories.stream()
            .map(e -> e.stream().reduce(0, sum))
            .sorted(reverseOrder())
            .limit(count)
            .reduce(0, sum);
    }
    
    @Override
    public Integer solvePart1() {
        return solve(1);
    }

    @Override
    public Integer solvePart2() {
        return solve(3);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_01.create(TEST).solvePart1() == 24_000;
        assert AoC2022_01.create(TEST).solvePart2() == 45_000;

        final Puzzle puzzle = Aocd.puzzle(2022, 1);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_01.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_01.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "1000\r\n" +
        "2000\r\n" +
        "3000\r\n" +
        "\r\n"     +
        "4000\r\n" +
        "\r\n"     +
        "5000\r\n" +
        "6000\r\n" +
        "\r\n"     +
        "7000\r\n" +
        "8000\r\n" +
        "9000\r\n" +
        "\r\n"     +
        "10000"
    );
}
