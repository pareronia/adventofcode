import static com.github.pareronia.aoc.itertools.IterTools.enumerate;
import static com.github.pareronia.aoc.itertools.IterTools.product;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.List;

public class AoC2024_25 extends SolutionBase<AoC2024_25.Input, Long, String> {

    private AoC2024_25(final boolean debug) {
        super(debug);
    }

    public static final AoC2024_25 create() {
        return new AoC2024_25(false);
    }

    public static final AoC2024_25 createDebug() {
        return new AoC2024_25(true);
    }

    @Override
    protected Input parseInput(final List<String> inputs) {
        return Input.fromInputs(inputs);
    }

    @Override
    public Long solvePart1(final Input input) {
        log(input);
        return product(input.keys, input.locks).stream()
                .filter(pp -> (pp.first() & pp.second()) == 0)
                .count();
    }

    @Override
    public String solvePart2(final Input input) {
        return "🎄";
    }

    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "3"),
    })
    public void samples() {}

    public static void main(final String[] args) throws Exception {
        AoC2024_25.create().run();
    }

    private static final String TEST =
            """
            #####
            .####
            .####
            .####
            .#.#.
            .#...
            .....

            #####
            ##.##
            .#.##
            ...##
            ...#.
            ...#.
            .....

            .....
            #....
            #....
            #...#
            #.#.#
            #.###
            #####

            .....
            .....
            #.#..
            ###..
            ###.#
            ###.#
            #####

            .....
            .....
            .....
            #....
            #.#..
            #.#.#
            #####
            """;

    record Input(List<Long> keys, List<Long> locks) {

        public static Input fromInputs(final List<String> inputs) {
            final List<Long> ks = new ArrayList<>();
            final List<Long> ls = new ArrayList<>();
            for (final List<String> block : StringOps.toBlocks(inputs)) {
                final int h = block.size();
                final long n =
                        enumerate(block.stream().skip(1)).stream()
                                .flatMapToLong(
                                        e -> {
                                            final int r = e.index();
                                            final String line = e.value();
                                            return enumerate(Utils.asCharacterStream(line)).stream()
                                                    .filter(ee -> ee.value() == '#')
                                                    .mapToLong(ee -> 1L << ((h * ee.index()) + r));
                                        })
                                .sum();
                if (block.get(0).charAt(0) == '#') {
                    ls.add(n);
                } else {
                    ks.add(n);
                }
            }
            return new Input(ks, ls);
        }
    }
}
