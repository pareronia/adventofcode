import java.util.Arrays;
import java.util.List;
import java.util.function.IntUnaryOperator;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2017_05 extends SolutionBase<int[], Integer, Integer> {

    private AoC2017_05(final boolean debug) {
        super(debug);
    }

    public static AoC2017_05 create() {
        return new AoC2017_05(false);
    }

    public static AoC2017_05 createDebug() {
        return new AoC2017_05(true);
    }
 
    @Override
    protected int[] parseInput(final List<String> inputs) {
        return inputs.stream().mapToInt(Integer::parseInt).toArray();
    }

    private int countJumps(
            final int[] input,
            final IntUnaryOperator jumpCalculator
    ) {
        final int[] offsets = Arrays.copyOf(input, input.length);
        int cnt = 0;
        int i = 0;
        while (i < offsets.length) {
            final int jump = offsets[i];
            offsets[i] = jumpCalculator.applyAsInt(jump);
            i += jump;
            cnt++;
        }
        return cnt;
    }
    
    @Override
    public Integer solvePart1(final int[] input) {
        return countJumps(input, jump -> jump + 1);
    }
    
    @Override
    public Integer solvePart2(final int[] input) {
        return countJumps(input, jump -> jump >= 3 ? jump - 1 : jump + 1);
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "5"),
        @Sample(method = "part2", input = TEST, expected = "10"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2017_05.create().run();
    }
    
    private static final String TEST = """
            0
            3
            0
            1
            -3
            """;
}
