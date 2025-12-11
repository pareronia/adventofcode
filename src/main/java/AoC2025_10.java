import static com.github.pareronia.aoc.Utils.toAString;
import static com.github.pareronia.aoc.itertools.IterTools.combinations;

import static java.util.stream.Collectors.joining;

import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToLongFunction;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2025_10 extends SolutionBase<List<AoC2025_10.Machine>, Long, Long> {

    private AoC2025_10(final boolean debug) {
        super(debug);
    }

    public static AoC2025_10 create() {
        return new AoC2025_10(false);
    }

    public static AoC2025_10 createDebug() {
        return new AoC2025_10(true);
    }

    @Override
    protected List<Machine> parseInput(final List<String> inputs) {
        return inputs.stream().map(Machine::fromInput).toList();
    }

    @Override
    public Long solvePart1(final List<Machine> machines) {
        final ToLongFunction<Machine> buttonPressesForLights =
                machine ->
                        Range.between(1, machine.presses().size())
                                .intStream()
                                .filter(
                                        n ->
                                                combinations(machine.presses().size(), n).stream()
                                                        .anyMatch(machine::pressesMatchLights))
                                .findFirst()
                                .orElseThrow();
        return machines.stream().mapToLong(buttonPressesForLights).sum();
    }

    @Override
    public Long solvePart2(final List<Machine> machines) {
        return 0L;
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "7"),
        @Sample(method = "part2", input = TEST, expected = "0"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
            [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
            [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
            """;

    record Machine(String lights, int[] joltages, List<int[]> presses) {

        public static Machine fromInput(final String input) {
            final String[] splits = input.split(" ");
            final String lights =
                    Utils.asCharacterStream(splits[0].substring(1, splits[0].length() - 1))
                            .map(ch -> (char) ('0' + ".#".indexOf(ch)))
                            .collect(toAString());
            final List<int[]> presses = new ArrayList<>();
            for (int i = 1; i < splits.length - 1; i++) {
                presses.add(
                        Arrays.stream(splits[i].substring(1, splits[i].length() - 1).split(","))
                                .mapToInt(Integer::parseInt)
                                .toArray());
            }
            final String sj = splits[splits.length - 1];
            final int[] joltages =
                    Arrays.stream(sj.substring(1, sj.length() - 1).split(","))
                            .mapToInt(Integer::parseInt)
                            .toArray();
            return new Machine(lights, joltages, presses);
        }

        private int pressAsBinary(final int idx) {
            return Arrays.stream(this.presses.get(idx))
                    .map(i -> 1 << (this.lights.length() - 1 - i))
                    .sum();
        }

        public boolean pressesMatchLights(final int... idxs) {
            return Arrays.stream(idxs).map(this::pressAsBinary).reduce(0, (acc, c) -> acc ^ c)
                    == Integer.parseInt(this.lights, 2);
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(100);
            builder.append("Machine [lights=")
                    .append(lights)
                    .append(", joltages=")
                    .append(Arrays.toString(joltages))
                    .append(", presses=[")
                    .append(presses.stream().map(Arrays::toString).collect(joining(", ")))
                    .append("]]");
            return builder.toString();
        }
    }
}
