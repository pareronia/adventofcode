import static com.github.pareronia.aoc.Utils.concat;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.IntGrid;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2024_10 extends SolutionBase<IntGrid, Integer, Integer> {

    private AoC2024_10(final boolean debug) {
        super(debug);
    }

    public static AoC2024_10 create() {
        return new AoC2024_10(false);
    }

    public static AoC2024_10 createDebug() {
        return new AoC2024_10(true);
    }

    @Override
    protected IntGrid parseInput(final List<String> inputs) {
        return IntGrid.from(inputs);
    }

    private int solve(final IntGrid grid, final Grading grading) {
    	@SuppressWarnings("PMD.AtLeastOneConstructor")
        class BFS {
            private static final int TARGET = 10;

            public List<List<Cell>> bfs(final Cell trailHead) {
                final List<List<Cell>> trails = new ArrayList<>();
                final Deque<List<Cell>> q = new ArrayDeque<>(List.of(List.of(trailHead)));
                while (!q.isEmpty()) {
                    final List<Cell> curr = q.pop();
                    if (curr.size() == TARGET) {
                        trails.add(curr);
                        continue;
                    }
                    final int next = grid.getValue(curr.getLast()) + 1;
                    grid.getCapitalNeighbours(curr.getLast())
                            .filter(n -> grid.getValue(n) == next)
                            .forEach(n -> q.add(concat(curr, n)));
                }
                return trails;
            }
        }

        final BFS bfs = new BFS();
        return grid.getAllEqualTo(0).mapToInt(trailhead -> grading.get(bfs.bfs(trailhead))).sum();
    }

    @Override
    public Integer solvePart1(final IntGrid grid) {
        return solve(grid, Grading.SCORE);
    }

    @Override
    public Integer solvePart2(final IntGrid grid) {
        return solve(grid, Grading.RATING);
    }

    enum Grading {
        SCORE,
        RATING;

        public int get(final List<List<Cell>> trails) {
            return switch (this) {
                case SCORE -> (int) trails.stream().map(List::getLast).distinct().count();
                case RATING -> trails.size();
            };
        }
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "36"),
        @Sample(method = "part2", input = TEST, expected = "81"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732
            """;
}
