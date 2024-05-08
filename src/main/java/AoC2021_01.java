import java.util.List;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2021_01 extends SolutionBase<List<Integer>, Long, Long> {

    private AoC2021_01(final boolean debug) {
        super(debug);
    }

    public static final AoC2021_01 create() {
        return new AoC2021_01(false);
    }

    public static final AoC2021_01 createDebug() {
        return new AoC2021_01(true);
    }

    @Override
    protected List<Integer> parseInput(final List<String> inputs) {
        return inputs.stream().map(Integer::valueOf).toList();
    }

    private long countIncreases(final List<Integer> depths, final int window) {
        return IntStream.range(window, depths.size())
                .filter(i -> depths.get(i) > depths.get(i - window))
                .count();
    }

    @Override
    public Long solvePart1(final List<Integer> depths) {
        return countIncreases(depths, 1);
    }

    @Override
    public Long solvePart2(final List<Integer> depths) {
        return countIncreases(depths, 3);
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "7"),
        @Sample(method = "part2", input = TEST, expected = "5"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2021_01.create().run();
    }

    private static final String TEST = """
            199
            200
            208
            210
            200
            207
            240
            269
            260
            263
            """;
}
