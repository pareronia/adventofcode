import static java.util.Collections.nCopies;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2021_06 extends SolutionBase<List<Integer>, Long, Long> {
    
    private AoC2021_06(final boolean debug) {
        super(debug);
    }

    public static final AoC2021_06 create() {
        return new AoC2021_06(false);
    }

    public static final AoC2021_06 createDebug() {
        return new AoC2021_06(true);
    }

    @Override
    protected List<Integer> parseInput(final List<String> inputs) {
        return Arrays.stream(inputs.get(0).split(","))
                .map(Integer::valueOf)
                .toList();
    }

    private long sumValues(final List<Long> list) {
        return list.stream().mapToLong(Long::longValue).sum();
    }

    private long solve(final List<Integer> initial, final int days) {
       final LinkedList<Long> fishies = new LinkedList<>(nCopies(9, 0L));
        for (final Integer i : initial) {
            fishies.set(i, fishies.get(i) + 1);
        }
        log(fishies, 0);
        for (int i = 0; i < days; i++) {
            final long zeroes = fishies.pollFirst();
            fishies.addLast(zeroes);
            fishies.set(6, fishies.get(6) + zeroes);
            log(fishies, i + 1);
        }
        return sumValues(fishies);
    }

    private void log(final LinkedList<Long> fishies, final int day) {
        log(() -> "(" + day + ") " + sumValues(fishies) + ": " + fishies);
    }
    
    @Override
    public Long solvePart1(final List<Integer> initial) {
        return solve(initial, 80);
    }

    @Override
    public Long solvePart2(final List<Integer> initial) {
        return solve(initial, 256);
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "5934", debug = false),
        @Sample(method = "part2", input = TEST, expected = "26984457539", debug = false),
    })
    public static void main(final String[] args) throws Exception {
        AoC2021_06.create().run();
    }

    private static final String TEST = "3,4,3,1,2";
}
