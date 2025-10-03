import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AoC2024_19 extends SolutionBase<AoC2024_19.Input, Long, Long> {

    private AoC2024_19(final boolean debug) {
        super(debug);
    }

    public static AoC2024_19 create() {
        return new AoC2024_19(false);
    }

    public static AoC2024_19 createDebug() {
        return new AoC2024_19(true);
    }

    @Override
    protected Input parseInput(final List<String> inputs) {
        return Input.fromInput(inputs);
    }

    private long count(
            final Map<String, Long> cache, final String design, final List<String> towels) {
        if (cache.containsKey(design)) {
            return cache.get(design);
        }
        if (design.length() == 0) {
            return 1L;
        }
        final long ans =
                towels.stream()
                        .filter(t -> design.startsWith(t))
                        .mapToLong(t -> count(cache, design.substring(t.length()), towels))
                        .sum();
        cache.put(design, ans);
        return ans;
    }

    @Override
    public Long solvePart1(final Input input) {
        final Map<String, Long> cache = new HashMap<>();
        return input.designs.stream()
                .mapToLong(d -> count(cache, d, input.towels))
                .filter(c -> c > 0)
                .count();
    }

    @Override
    public Long solvePart2(final Input input) {
        final Map<String, Long> cache = new HashMap<>();
        return input.designs.stream().mapToLong(d -> count(cache, d, input.towels)).sum();
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "6"),
        @Sample(method = "part2", input = TEST, expected = "16")
    })
    public void samples() {}

    public static void main(final String[] args) throws Exception {
        AoC2024_19.create().run();
    }

    private static final String TEST =
            """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
            """;

    record Input(List<String> towels, List<String> designs) {

        public static Input fromInput(final List<String> inputs) {
            final List<List<String>> blocks = StringOps.toBlocks(inputs);
            return new Input(
                    Arrays.stream(blocks.get(0).get(0).split(", ")).toList(), blocks.get(1));
        }
    }
}
