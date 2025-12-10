import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import com.github.pareronia.aoc.itertools.IterTools;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class AoC2024_02
        extends SolutionBase<List<List<Integer>>, Integer, Integer> {
    
    private AoC2024_02(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_02 create() {
        return new AoC2024_02(false);
    }
    
    public static AoC2024_02 createDebug() {
        return new AoC2024_02(true);
    }
    
    @Override
    protected List<List<Integer>> parseInput(final List<String> inputs) {
        return inputs.stream()
            .map(line -> Arrays.stream(line.split(" "))
                    .map(Integer::parseInt)
                    .toList())
            .toList();
    }
    
    private boolean safe(final List<Integer> levels) {
        final List<Integer> diffs = IterTools.pairwise(levels.iterator()).stream()
            .map(w -> w.second() - w.first())
            .toList();
        return diffs.stream().allMatch(diff -> 1 <= diff && diff <= 3)
            || diffs.stream().allMatch(diff -> -1 >= diff && diff >= -3);
    }
    
    @Override
    public Integer solvePart1(final List<List<Integer>> reports) {
        return (int) reports.stream().filter(this::safe).count();
    }
    
    @Override
    public Integer solvePart2(final List<List<Integer>> reports) {
        return (int) reports.stream()
            .filter(report ->
                range(report.size()).intStream()
                    .mapToObj(i -> {
                        final List<Integer> tmp = new ArrayList<>(report);
                        tmp.remove(i);
                        return tmp;
                    })
                    .anyMatch(this::safe)
            ).count();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "2"),
        @Sample(method = "part2", input = TEST, expected = "4"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_02.create().run();
    }

    private static final String TEST = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """;
}