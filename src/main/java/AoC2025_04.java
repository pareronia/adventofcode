import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2025_04 extends SolutionBase<CharGrid, Long, Long> {

    public static final char ROLL = '@';
    public static final char EMPTY = '.';

    private AoC2025_04(final boolean debug) {
        super(debug);
    }

    public static AoC2025_04 create() {
        return new AoC2025_04(false);
    }

    public static AoC2025_04 createDebug() {
        return new AoC2025_04(true);
    }

    @Override
    protected CharGrid parseInput(final List<String> inputs) {
        return CharGrid.from(inputs);
    }

    private boolean isRemovable(final CharGrid grid, final Cell cell) {
        return grid.getValue(cell) == ROLL
                && grid.getAllNeighbours(cell).filter(n -> grid.getValue(n) == ROLL).count() < 4;
    }

    @Override
    public Long solvePart1(final CharGrid grid) {
        return grid.getCells().filter(cell -> isRemovable(grid, cell)).count();
    }

    @Override
    public Long solvePart2(final CharGrid grid) {
        final Deque<Cell> queue = new ArrayDeque<>();
        grid.getCells().filter(cell -> isRemovable(grid, cell)).forEach(queue::add);
        final Set<Cell> seen = new HashSet<>();
        while (!queue.isEmpty()) {
            final Cell cell = queue.poll();
            if (seen.contains(cell)) {
                continue;
            }
            seen.add(cell);
            grid.setValue(cell, EMPTY);
            grid.getAllNeighbours(cell).filter(n -> isRemovable(grid, n)).forEach(queue::add);
        }
        return (long) seen.size();
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "13"),
        @Sample(method = "part2", input = TEST, expected = "43"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            ..@@.@@@@.
            @@@.@.@.@@
            @@@@@.@.@@
            @.@@@@..@.
            @@.@@@@.@@
            .@@@@@@@.@
            .@.@.@.@@@
            @.@@@.@@@@
            .@@@@@@@@.
            @.@.@@@.@.
            """;
}
