import static com.github.pareronia.aoc.IterTools.zip;
import static com.github.pareronia.aoc.ListUtils.sorted;

import java.util.List;

import com.github.pareronia.aoc.Counter;
import com.github.pareronia.aoc.ListUtils;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_01
        extends SolutionBase<AoC2024_01.Lists, Integer, Integer> {
    
    private AoC2024_01(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_01 create() {
        return new AoC2024_01(false);
    }
    
    public static AoC2024_01 createDebug() {
        return new AoC2024_01(true);
    }
    
    @Override
    protected Lists parseInput(final List<String> inputs) {
        final List<List<Integer>> lists = inputs.stream()
                .map(line -> StringOps.splitOnce(line, "\s+"))
                .map(splits -> List.of(
                        Integer.valueOf(splits.left()),
                        Integer.valueOf(splits.right())))
                .toList();
        final List<List<Integer>> transpose = ListUtils.transpose(lists);
        return new Lists(transpose.get(0), transpose.get(1));
    }
    
    @Override
    public Integer solvePart1(final Lists lists) {
        return Utils.stream(zip(sorted(lists.left), sorted(lists.right)))
                .mapToInt(z -> Math.abs(z.first() - z.second()))
                .sum();
    }
    
    @Override
    public Integer solvePart2(final Lists lists) {
        final Counter<Integer> counter = new Counter<>(lists.right);
        return lists.left.stream()
                .mapToInt(n -> n * counter.get(n).intValue())
                .sum();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "11"),
        @Sample(method = "part2", input = TEST, expected = "31"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_01.create().run();
    }

    private static final String TEST = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
            """;

    record Lists(List<Integer> left, List<Integer> right) {}
}