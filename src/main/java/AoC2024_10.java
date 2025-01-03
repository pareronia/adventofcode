import static com.github.pareronia.aoc.Utils.last;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.IntGrid;
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
    
    private List<List<Cell>> getTrails(final IntGrid grid) {
        class BFS {
            private final List<List<Cell>> trails = new ArrayList<>();
            
            void bfs(final List<Cell> trail) {
               final Deque<List<Cell>> q = new ArrayDeque<>(List.of(trail));
               while (!q.isEmpty()) {
                   final List<Cell> curr = q.pop();
                   if (curr.size() == 10) {
                       trails.add(curr);
                       continue;
                   }
                   final int next = grid.getValue(last(curr)) + 1;
                   grid.getCapitalNeighbours(last(curr))
                       .filter(n -> grid.getValue(n) == next)
                       .forEach(n -> {
                           final List<Cell> newTrail = new ArrayList<>(curr);
                           newTrail.add(n);
                           q.add(newTrail);
                   });
               }
            }
        }
        
        final BFS bfs = new BFS();
        grid.getAllEqualTo(0).forEach(zero -> bfs.bfs(List.of(zero)));
        return bfs.trails;
    }
    
    @Override
    public Integer solvePart1(final IntGrid grid) {
        final List<List<Cell>> trails = getTrails(grid);
        return trails.stream()
                .map(trail -> trail.get(0))
                .distinct()
                .mapToInt(zero -> (int) trails.stream()
                            .filter(trail -> trail.get(0).equals(zero))
                            .map(trail -> trail.get(9))
                            .distinct()
                            .count())
                .sum();
    }
    
    @Override
    public Integer solvePart2(final IntGrid grid) {
        return getTrails(grid).size();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "36"),
        @Sample(method = "part2", input = TEST, expected = "81"),
    })
    public void samples() {
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