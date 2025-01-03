import static com.github.pareronia.aoc.Utils.concat;
import static com.github.pareronia.aoc.Utils.last;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.IntGrid;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

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
        class BFS {
            static List<List<Cell>> bfs(
                    final IntGrid grid, final Cell trailHead
            ) {
                final List<List<Cell>> trails = new ArrayList<>();
                final Deque<List<Cell>> q
                        = new ArrayDeque<>(List.of(List.of(trailHead)));
                while (!q.isEmpty()) {
                    final List<Cell> curr = q.pop();
                    if (curr.size() == 10) {
                        trails.add(curr);
                        continue;
                    }
                    final int next = grid.getValue(last(curr)) + 1;
                    grid.getCapitalNeighbours(last(curr))
                        .filter(n -> grid.getValue(n) == next)
                        .forEach(n -> q.add(concat(curr, n)));
                }
                return trails;
            }
        }

        return grid.getAllEqualTo(0)
            .mapToInt(trailhead -> grading.get(BFS.bfs(grid, trailhead)))
            .sum();
    }
    
    @Override
    public Integer solvePart1(final IntGrid grid) {
        return solve(grid, Grading.SCORE);
    }
    
    @Override
    public Integer solvePart2(final IntGrid grid) {
        return solve(grid, Grading.RATING);
    }

    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "36"),
        @Sample(method = "part2", input = TEST, expected = "81"),
    })
    public void samples() {
    }
    
    enum Grading {
        SCORE, RATING;
        
        public int get(final List<List<Cell>> trails) {
            return switch (this) {
                case SCORE -> (int) trails.stream()
                                    .map(Utils::last)
                                    .distinct().count();
                case RATING -> trails.size();
            };
        }
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_10.create().run();
    }

    private static final String TEST = """
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