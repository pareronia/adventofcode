import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_20 extends SolutionBase<CharGrid, Integer, Integer> {

    private AoC2024_20(final boolean debug) {
        super(debug);
    }

    public static AoC2024_20 create() {
        return new AoC2024_20(false);
    }

    public static AoC2024_20 createDebug() {
        return new AoC2024_20(true);
    }

    @Override
    protected CharGrid parseInput(final List<String> inputs) {
        return CharGrid.from(inputs);
    }

    private Integer solve(final CharGrid grid, final int cheatLength, final int target) {
        final Cell start = grid.getAllEqualTo('S').findFirst().orElseThrow();
        final Cell end = grid.getAllEqualTo('E').findFirst().orElseThrow();
        Direction dir =
                Direction.CAPITAL.stream()
                        .filter(d -> grid.getValue(start.at(d)) != '#')
                        .findFirst()
                        .orElseThrow();
        Cell pos = start;
        int dist = 0;
        final List<Cell> track = new ArrayList<>();
        final int h = grid.getHeight();
        final int[] d = new int[grid.getWidth() * h];
        Arrays.fill(d, Integer.MAX_VALUE);
        while (true) {
            d[pos.getRow() * h + pos.getCol()] = dist;
            track.add(pos);
            if (pos.equals(end)) {
                break;
            }
            final Cell p = pos;
            dir =
                    List.of(dir, dir.turn(Turn.RIGHT), dir.turn(Turn.LEFT)).stream()
                            .filter(dd -> grid.getValue(p.at(dd)) != '#')
                            .findFirst()
                            .orElseThrow();
            pos = pos.at(dir);
            dist += 1;
        }
        int ans = 0;
        for (final Cell cell : track) {
            final int dCell = d[cell.getRow() * h + cell.getCol()];
            for (int md = 2; md <= cheatLength; md++) {
                final int minReq = target + md;
                ans +=
                        cell.allAtManhattanDistance(md)
                                .filter(n -> grid.isInBounds(n))
                                .filter(
                                        n -> {
                                            final int dN = d[n.getRow() * h + n.getCol()];
                                            return dN != Integer.MAX_VALUE && dN - dCell >= minReq;
                                        })
                                .count();
            }
        }
        return ans;
    }

    @Override
    public Integer solvePart1(final CharGrid grid) {
        return solve(grid, 2, 100);
    }

    @Override
    public Integer solvePart2(final CharGrid grid) {
        return solve(grid, 20, 100);
    }

    @Override
    public void samples() {
        final AoC2024_20 test = AoC2024_20.createDebug();
        final CharGrid input = test.parseInput(StringOps.splitLines(TEST));
        assert test.solve(input, 2, 2) == 44;
        assert test.solve(input, 20, 50) == 285;
    }

    public static void main(final String[] args) throws Exception {
        AoC2024_20.create().run();
    }

    private static final String TEST =
            """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #S#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###..E#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############
            """;
}
