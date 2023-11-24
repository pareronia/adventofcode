import static com.github.pareronia.aoc.SetUtils.disjunction;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aoc.geometry3d.Cuboid;
import com.github.pareronia.aoc.geometry3d.Position3D;
import com.github.pareronia.aoc.graph.BFS;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_18 extends AoCBase {
    
    private final Set<Position3D> cubes;
    
    private AoC2022_18(final List<String> input, final boolean debug) {
        super(debug);
        this.cubes = input.stream()
            .map(line -> line.split(","))
            .map(splits -> Arrays.stream(splits).mapToInt(Integer::parseInt).toArray())
            .map(nums -> Position3D.of(nums[0], nums[1], nums[2]))
            .collect(toSet());
    }
    
    public static final AoC2022_18 create(final List<String> input) {
        return new AoC2022_18(input, false);
    }

    public static final AoC2022_18 createDebug(final List<String> input) {
        return new AoC2022_18(input, true);
    }
    
    private int surfaceArea(final Set<Position3D> cubes) {
        return (int) cubes.stream()
            .flatMap(Position3D::capitalNeighbours)
            .filter(n -> !cubes.contains(n))
            .count();
    }
    
    private Bounds getBounds() {
        final var statsX = cubes.stream()
                .mapToInt(Position3D::getX).summaryStatistics();
        final var statsY = cubes.stream()
                .mapToInt(Position3D::getY).summaryStatistics();
        final var statsZ = cubes.stream()
                .mapToInt(Position3D::getZ).summaryStatistics();
        return new Bounds(
            Range.between(statsX.getMin(), statsX.getMax()),
            Range.between(statsY.getMin(), statsY.getMax()),
            Range.between(statsZ.getMin(), statsZ.getMax())
        );
    }

    private Set<Position3D> findOutside(final Bounds bounds) {
        final var start = Position3D.of(
                bounds.x.getMinimum() - 1,
                bounds.y.getMinimum() - 1,
                bounds.z.getMinimum() - 1);
        final var searchSpace = Cuboid.of(
            bounds.x.getMinimum() - 1, bounds.x.getMaximum() + 1,
            bounds.y.getMinimum() - 1, bounds.y.getMaximum() + 1,
            bounds.z.getMinimum() - 1, bounds.z.getMaximum() + 1);
        final Function<Position3D, Stream<Position3D>> adjacent =
            pos -> pos.capitalNeighbours()
                    .filter(searchSpace::contains)
                    .filter(n -> !this.cubes.contains(n));
        return BFS.floodFill(start, adjacent);
    }
    
    private Set<Position3D> findInside(final Bounds bounds) {
        final var outside = findOutside(bounds);
        final Cuboid cuboid = Cuboid.of(
            bounds.x.getMinimum(), bounds.x.getMaximum(),
            bounds.y.getMinimum(), bounds.y.getMaximum(),
            bounds.z.getMinimum(), bounds.z.getMaximum());
        return cuboid.positions()
            .filter(pos -> !this.cubes.contains(pos) && !outside.contains(pos))
            .collect(toSet());
    }

    @Override
    public Integer solvePart1() {
        return surfaceArea(this.cubes);
    }

    @Override
    public Integer solvePart2() {
        final var inside = findInside(getBounds());
        return surfaceArea(disjunction(this.cubes, inside));
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_18.createDebug(TEST1).solvePart1() == 10;
        assert AoC2022_18.createDebug(TEST2).solvePart1() == 64;
        assert AoC2022_18.createDebug(TEST2).solvePart2() == 58;

        final Puzzle puzzle = Aocd.puzzle(2022, 18);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_18.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_18.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST1 = splitLines("""
        1,1,1
        2,1,1
        """);
    private static final List<String> TEST2 = splitLines("""
        2,2,2
        1,2,2
        3,2,2
        2,1,2
        2,3,2
        2,2,1
        2,2,3
        2,2,4
        2,2,6
        1,2,5
        3,2,5
        2,1,5
        2,3,5
        """);
    
    private static final record Bounds (
            Range x,
            Range y,
            Range z
    ) {
    }
}
