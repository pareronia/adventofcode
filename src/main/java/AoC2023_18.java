import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2023_18 extends SolutionBase<List<String>, Long, Long> {

    private static final Direction[] DIRS = {
        Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP,
    };

    private AoC2023_18(final boolean debug) {
        super(debug);
    }

    public static AoC2023_18 create() {
        return new AoC2023_18(false);
    }

    public static AoC2023_18 createDebug() {
        return new AoC2023_18(true);
    }

    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }

    private long solve(final List<Instruction> instructions) {
        final List<Position> vertices = new ArrayList<>(List.of(Position.ORIGIN));
        instructions.forEach(
                instruction -> {
                    vertices.add(
                            vertices.getLast()
                                    .translate(instruction.direction(), instruction.amount()));
                });
        return new Polygon(vertices).insideArea();
    }

    @Override
    public Long solvePart1(final List<String> input) {
        final List<Instruction> instructions =
                input.stream()
                        .map(
                                line -> {
                                    final String[] splits = line.split(" ");
                                    return new Instruction(
                                            Direction.fromString(splits[0]),
                                            Integer.parseInt(splits[1]));
                                })
                        .toList();
        return solve(instructions);
    }

    @Override
    public Long solvePart2(final List<String> input) {
        final List<Instruction> instructions =
                input.stream()
                        .map(
                                line -> {
                                    final String[] splits = line.split(" ");
                                    return new Instruction(
                                            DIRS[Integer.parseInt(splits[2].substring(7, 8))],
                                            Integer.parseInt(splits[2].substring(2, 7), 16));
                                })
                        .toList();
        return solve(instructions);
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "62"),
        @Sample(method = "part2", input = TEST, expected = "952408144115"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    record Instruction(Direction direction, int amount) {}

    record Polygon(List<Position> vertices) {

        private long shoelace() {
            final int size = vertices.size();
            final long s =
                    range(vertices.size())
                            .intStream()
                            .mapToLong(
                                    i -> {
                                        final Position vertex = vertices.get(i);
                                        final Position next = vertices.get((i + 1) % size);
                                        final Position prev = vertices.get((size + i - 1) % size);
                                        return ((long) vertex.getX()) * (next.getY() - prev.getY());
                                    })
                            .sum();
            return Math.abs(s) / 2;
        }

        private long circumference() {
            return range(1, vertices.size(), 1)
                    .intStream()
                    .mapToLong(i -> vertices.get(i).manhattanDistance(vertices.get(i - 1)))
                    .sum();
        }

        public long insideArea() {
            return this.shoelace() + this.circumference() / 2 + 1;
        }
    }

    private static final String TEST =
            """
            R 6 (#70c710)
            D 5 (#0dc571)
            L 2 (#5713f0)
            D 2 (#d2c081)
            R 2 (#59c680)
            D 2 (#411b91)
            L 5 (#8ceee2)
            U 2 (#caa173)
            L 1 (#1b58a2)
            U 2 (#caa171)
            R 2 (#7807d2)
            U 3 (#a77fa3)
            L 2 (#015232)
            U 2 (#7a21e3)
            """;
}
