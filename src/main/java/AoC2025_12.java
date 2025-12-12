import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2025_12 extends SolutionBase<AoC2025_12.Input, Long, String> {

    private AoC2025_12(final boolean debug) {
        super(debug);
    }

    public static AoC2025_12 create() {
        return new AoC2025_12(false);
    }

    public static AoC2025_12 createDebug() {
        return new AoC2025_12(true);
    }

    @Override
    protected Input parseInput(final List<String> inputs) {
        return Input.fromInput(inputs);
    }

    private long maximumAreaRequired(final Region region, final Map<Integer, Shape> shapes) {
        return region.quantities().entrySet().stream()
                .mapToLong(e -> e.getValue() * shapes.get(e.getKey()).area())
                .sum();
    }

    @Override
    public Long solvePart1(final Input input) {
        return input.regions().stream()
                .filter(r -> maximumAreaRequired(r, input.shapes()) <= r.height() * r.width())
                .count();
    }

    @Override
    public String solvePart2(final Input input) {
        return "🎄";
    }

    @Samples({
        //        @Sample(method = "part1", input = TEST, expected = "2"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    @SuppressWarnings("unused")
    private static final String TEST =
            """
            0:
            ###
            ##.
            ##.

            1:
            ###
            ##.
            .##

            2:
            .##
            ###
            ##.

            3:
            ##.
            ###
            ##.

            4:
            ###
            #..
            ###

            5:
            ###
            .#.
            ###

            4x4: 0 0 0 0 2 0
            12x5: 1 0 1 0 2 2
            12x5: 1 0 1 0 3 2
            """;

    record Shape(int area) {

        public static Shape fromInput(final List<String> inputs) {
            return new Shape(
                    inputs.size() * inputs.stream().mapToInt(String::length).max().getAsInt());
        }
    }

    record Region(int width, int height, Map<Integer, Integer> quantities) {

        public static Region fromInput(final String input) {
            final StringSplit split = StringOps.splitOnce(input, ": ");
            final StringSplit splitSize = StringOps.splitOnce(split.left(), "x");
            final String[] aq = split.right().split(" ");
            final Map<Integer, Integer> quantities =
                    range(aq.length).stream().collect(toMap(i -> i, i -> Integer.parseInt(aq[i])));
            return new Region(
                    Integer.parseInt(splitSize.left()),
                    Integer.parseInt(splitSize.right()),
                    quantities);
        }
    }

    record Input(Map<Integer, Shape> shapes, Set<Region> regions) {

        public static Input fromInput(final List<String> inputs) {
            final List<List<String>> blocks = StringOps.toBlocks(inputs);
            final Map<Integer, Shape> shapes = new HashMap<>();
            for (int i = 0; i < blocks.size() - 1; i++) {
                final String first = blocks.get(i).getFirst();
                final int idx = Integer.parseInt(first.substring(0, first.length() - 1));
                shapes.put(idx, Shape.fromInput(blocks.get(i).subList(1, blocks.get(i).size())));
            }
            final Set<Region> regions =
                    blocks.getLast().stream().map(Region::fromInput).collect(toSet());
            return new Input(shapes, regions);
        }
    }
}
