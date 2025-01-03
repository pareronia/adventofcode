import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_05
        extends SolutionBase<AoC2024_05.Input, Integer, Integer> {
    
    private AoC2024_05(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_05 create() {
        return new AoC2024_05(false);
    }
    
    public static AoC2024_05 createDebug() {
        return new AoC2024_05(true);
    }
    
    @Override
    protected Input parseInput(final List<String> inputs) {
        final List<List<String>> blocks = StringOps.toBlocks(inputs);
        final Map<Integer, List<Integer>> order = new HashMap<>();
        for (final String line : blocks.get(0)) {
            final StringSplit split = StringOps.splitOnce(line, "\\|");
            order.computeIfAbsent(
                    Integer.valueOf(split.left()), k -> new ArrayList<>())
                .add(Integer.valueOf(split.right()));
        }
        final List<List<Integer>> updates = blocks.get(1).stream()
                .map(line -> Arrays.stream(line.split(","))
                                .map(Integer::valueOf)
                                .toList())
                .toList();
        return new Input(order, updates);
    }
    
    private int solve(final Input input, final Mode mode) {
        final Comparator<Integer> comparator
            = (a, b) -> (
            input.order.getOrDefault(a, new ArrayList<>()).contains(b)
            ? -1 : 1);
        int ans = 0;
        for (final List<Integer> update : input.updates) {
           final List<Integer> correct = new ArrayList<>(update);
           Collections.sort(correct, comparator);
           if (!(mode == Mode.USE_CORRECT ^ update.equals(correct))) {
               ans += correct.get(correct.size() / 2);
           }
        }
        return ans;
    }
    
    @Override
    public Integer solvePart1(final Input input) {
        return solve(input, Mode.USE_CORRECT);
    }
    
    @Override
    public Integer solvePart2(final Input input) {
        return solve(input, Mode.USE_INCORRECT);
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "143"),
        @Sample(method = "part2", input = TEST, expected = "123"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_05.create().run();
    }

    private static final String TEST = """
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13

            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47
            """;

    record Input(
            Map<Integer, List<Integer>> order,
            List<List<Integer>> updates
    ) {
    }

    private enum Mode {
        USE_CORRECT,
        USE_INCORRECT;
    }
}