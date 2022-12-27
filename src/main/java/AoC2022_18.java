import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.SetUtils.disjunction;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.geometry3d.Cuboid;
import com.github.pareronia.aoc.geometry3d.Position3D;
import com.github.pareronia.aoc.geometry3d.Vector3D;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_18 extends AoCBase {
    
    private static final Set<Vector3D> DIRECTIONS = Set.of(
        Vector3D.of(-1, 0, 0), Vector3D.of(1, 0, 0),
        Vector3D.of(0, -1, 0), Vector3D.of(0, 1, 0),
        Vector3D.of(0, 0, -1), Vector3D.of(0, 0, 1)
    );
    
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
            .flatMap(cube -> DIRECTIONS.stream().map(cube::translate))
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
            statsX.getMin(), statsX.getMax(),
            statsY.getMin(), statsY.getMax(),
            statsZ.getMin(), statsZ.getMax()
        );
    }

    private Set<Position3D> findOutside(final Bounds bounds) {
        final var searchSpace = Cuboid.of(
            bounds.minX - 1, bounds.maxX + 1,
            bounds.minY - 1, bounds.maxY + 1,
            bounds.minZ - 1, bounds.maxZ + 1);
        final var outside = new HashSet<Position3D>();
        final var start = Position3D.of(
                bounds.minX - 1, bounds.minY - 1, bounds.minZ - 1);
        final var seen = new HashSet<>(Set.of(start));
        final var q = new ArrayDeque<>(Set.of(start));
        while (!q.isEmpty()) {
            final Position3D pos = q.poll();
            if (cubes.contains(pos)) {
                continue;
            }
            outside.add(pos);
            for (final Vector3D d : DIRECTIONS) {
                final Position3D n = pos.translate(d);
                if (searchSpace.contains(n) && !seen.contains(n)) {
                    q.add(n);
                    seen.add(n);
                }
            }
        }
        return outside;
    }
    
    private Set<Position3D> findInside(final Bounds bounds) {
        final var outside = findOutside(bounds);
        final Cuboid cuboid = Cuboid.of(
            bounds.minX, bounds.maxX,
            bounds.minY, bounds.maxY,
            bounds.minZ, bounds.maxZ);
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
            int minX,
            int maxX,
            int minY,
            int maxY,
            int minZ,
            int maxZ
    ) {
    }
}
