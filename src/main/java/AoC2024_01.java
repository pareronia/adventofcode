import static com.github.pareronia.aoc.IterTools.zip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.pareronia.aoc.Counter;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
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
        final List<Integer> left = new ArrayList<>();
        final List<Integer> right = new ArrayList<>();
        for (final String line : inputs) {
            final StringSplit splits = StringOps.splitOnce(line, "\s+");
            left.add(Integer.parseInt(splits.left()));
            right.add(Integer.parseInt(splits.right()));
        }
        return new Lists(left, right);
    }
    
    @Override
    public Integer solvePart1(final Lists lists) {
        Collections.sort(lists.left);
        Collections.sort(lists.right);
        return Utils.stream(zip(lists.left, lists.right).iterator())
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