import static java.util.stream.Collectors.toList;

import java.util.List;

import com.github.pareronia.aoc.MutableInt;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_13 extends AoCBase {
    
    private final List<Layer> layers;

    private AoC2017_13(final List<String> inputs, final boolean debug) {
        super(debug);
        this.layers = inputs.stream()
            .map(s -> {
                final String[] splits = s.split(": ");
                return new Layer(
                        Integer.parseInt(splits[0]),
                        Integer.parseInt(splits[1]));
            })
            .collect(toList());
    }

    public static AoC2017_13 create(final List<String> input) {
        return new AoC2017_13(input, false);
    }

    public static AoC2017_13 createDebug(final List<String> input) {
        return new AoC2017_13(input, true);
    }
    
    private boolean caught(final Layer layer, final int delay) {
        return (delay + layer.depth) % ((layer.range - 1) * 2) == 0;
    }
    
    @Override
    public Integer solvePart1() {
        return this.layers.stream()
            .filter(layer -> caught(layer, 0))
            .mapToInt(l -> l.depth * l.range)
            .sum();
    }
    
    @Override
    public Integer solvePart2() {
        final MutableInt delay = new MutableInt(0);
        while (this.layers.stream().anyMatch(layer -> caught(layer, delay.intValue()))) {
            delay.increment();
        }
        return delay.intValue();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_13.createDebug(TEST).solvePart1() == 24;
        assert AoC2017_13.createDebug(TEST).solvePart2() == 10;

        final Puzzle puzzle = Aocd.puzzle(2017, 13);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2017_13.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2017_13.create(puzzle.getInputData()).solvePart2())
        );
    }
    
    private static final List<String> TEST = splitLines(
            "0: 3\n" +
            "1: 2\n" +
            "4: 4\n" +
            "6: 4"
    );
    
    record Layer(int depth, int range) { }
}
