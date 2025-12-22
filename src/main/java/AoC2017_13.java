import com.github.pareronia.aoc.MutableInt;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2017_13 extends SolutionBase<List<AoC2017_13.Layer>, Integer, Integer> {

    private AoC2017_13(final boolean debug) {
        super(debug);
    }

    public static AoC2017_13 create() {
        return new AoC2017_13(false);
    }

    public static AoC2017_13 createDebug() {
        return new AoC2017_13(true);
    }

    @Override
    protected List<Layer> parseInput(final List<String> inputs) {
        return inputs.stream().map(Layer::fromInput).toList();
    }

    private boolean caught(final Layer layer, final int delay) {
        return (delay + layer.depth) % ((layer.range - 1) * 2) == 0;
    }

    @Override
    public Integer solvePart1(final List<Layer> layers) {
        return layers.stream()
                .filter(layer -> caught(layer, 0))
                .mapToInt(l -> l.depth * l.range)
                .sum();
    }

    @Override
    public Integer solvePart2(final List<Layer> layers) {
        final MutableInt delay = new MutableInt(0);
        while (layers.stream().anyMatch(layer -> caught(layer, delay.intValue()))) {
            delay.increment();
        }
        return delay.intValue();
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "24"),
        @Sample(method = "part2", input = TEST, expected = "10"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            0: 3
            1: 2
            4: 4
            6: 4
            """;

    record Layer(int depth, int range) {

        public static Layer fromInput(final String input) {
            final StringSplit sp = StringOps.splitOnce(input, ": ");
            return new Layer(Integer.parseInt(sp.left()), Integer.parseInt(sp.right()));
        }
    }
}
