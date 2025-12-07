import static java.util.stream.Collectors.toSet;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2025_07 extends SolutionBase<CharGrid, Long, Long> {

    public static final char SPLITTER = '^';
    public static final char START = 'S';

    private AoC2025_07(final boolean debug) {
        super(debug);
    }

    public static AoC2025_07 create() {
        return new AoC2025_07(false);
    }

    public static AoC2025_07 createDebug() {
        return new AoC2025_07(true);
    }

    @Override
    protected CharGrid parseInput(final List<String> inputs) {
        return CharGrid.from(inputs);
    }

    @Override
    public Long solvePart1(final CharGrid grid) {
        final Set<Integer> beams = grid.getAllEqualTo(START).map(Cell::getCol).collect(toSet());
        long ans = 0L;
        final Iterable<Cell> splitters = () -> grid.getAllEqualTo(SPLITTER).iterator();
        for (final Cell splitter : splitters) {
            if (beams.contains(splitter.getCol())) {
                ans++;
                beams.remove(splitter.getCol());
                for (final Direction direction : Set.of(Direction.LEFT, Direction.RIGHT)) {
                    beams.add(splitter.at(direction).getCol());
                }
            }
        }
        return ans;
    }

    @Override
    public Long solvePart2(final CharGrid grid) {
        class DFS {
            private final Cell start;
            private final Set<Cell> splitters;
            private final Map<Cell, Long> cache = new HashMap<>();

            DFS() {
                this.start = grid.getAllEqualTo(START).findFirst().orElseThrow();
                this.splitters = grid.getAllEqualTo(SPLITTER).collect(toSet());
                splitters.add(this.start);
            }

            long dfs(final Cell cell) {
                final long ans;
                if (this.cache.containsKey(cell)) {
                    ans = this.cache.get(cell);
                } else {
                    if (cell.equals(this.start)) {
                        ans = 1;
                    } else {
                        ans =
                                grid.getCellsN(cell)
                                        .takeWhile(n -> !this.splitters.contains(n))
                                        .flatMap(
                                                n ->
                                                        Stream.of(Direction.LEFT, Direction.RIGHT)
                                                                .map(n::at))
                                        .filter(this.splitters::contains)
                                        .mapToLong(this::dfs)
                                        .sum();
                    }
                    this.cache.put(cell, ans);
                }
                return ans;
            }
        }

        final Cell bottomLeft = Cell.at(grid.getMaxRowIndex(), 0);
        final DFS dfs = new DFS();
        return dfs.dfs(bottomLeft) + grid.getCellsE(bottomLeft).mapToLong(dfs::dfs).sum();
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "21"),
        @Sample(method = "part2", input = TEST, expected = "40"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            .......S.......
            ...............
            .......^.......
            ...............
            ......^.^......
            ...............
            .....^.^.^.....
            ...............
            ....^.^...^....
            ...............
            ...^.^...^.^...
            ...............
            ..^...^.....^..
            ...............
            .^.^.^.^.^...^.
            ...............
            """;
}
