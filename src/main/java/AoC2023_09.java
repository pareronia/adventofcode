import static com.github.pareronia.aoc.IterTools.zip;
import static com.github.pareronia.aoc.Utils.last;
import static com.github.pareronia.aoc.Utils.stream;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import com.github.pareronia.aoc.ListUtils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_09
        extends SolutionBase<List<List<Integer>>, Integer, Integer> {
    
    private AoC2023_09(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_09 create() {
        return new AoC2023_09(false);
    }
    
    public static AoC2023_09 createDebug() {
        return new AoC2023_09(true);
    }
    
    @Override
    protected List<List<Integer>> parseInput(final List<String> inputs) {
        return inputs.stream()
            .map(line ->
                Arrays.stream(line.split(" ")).map(Integer::valueOf).toList())
            .toList();
    }
    
    private int solve(final List<Integer> lineIn) {
        List<Integer> line = new ArrayList<>(lineIn);
        final Deque<Integer> tails = new ArrayDeque<>(List.of(last(line)));
        while (!line.stream().allMatch(tails.peekLast()::equals)) {
            line = stream(zip(line, line.subList(1, line.size())).iterator())
                .map(z -> z.second() - z.first())
                .toList();
            tails.addLast(last(line));
        }
        return tails.stream().mapToInt(Integer::valueOf).sum();
    }

    @Override
    public Integer solvePart1(final List<List<Integer>> input) {
        return input.stream()
            .mapToInt(this::solve)
            .sum();
    }
    
    @Override
    public Integer solvePart2(final List<List<Integer>> input) {
        return input.stream()
            .map(ListUtils::reversed)
            .mapToInt(this::solve)
            .sum();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "114"),
        @Sample(method = "part2", input = TEST, expected = "2"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_09.create().run();
    }

    private static final String TEST = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """;
}
