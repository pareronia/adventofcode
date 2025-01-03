import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.IntGrid;
import com.github.pareronia.aoc.SetUtils;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.geometry.Turn;
import com.github.pareronia.aoc.graph.Dijkstra;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_17
        extends SolutionBase<IntGrid, Integer, Integer> {
    
    private AoC2023_17(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_17 create() {
        return new AoC2023_17(false);
    }
    
    public static AoC2023_17 createDebug() {
        return new AoC2023_17(true);
    }
    
    @Override
    protected IntGrid parseInput(final List<String> inputs) {
        return IntGrid.from(inputs);
    }
    
    private int solve(final IntGrid grid, final int minMoves, final int maxMoves) {
        record Move(Cell cell, Direction dir, int cost) {}
        
        final Function<Move, Stream<Move>> adjacent = move -> {
            final Set<Direction> dirs = move.dir() == null
                ? Direction.CAPITAL
                : SetUtils.difference(Direction.CAPITAL,
                    Set.of(move.dir(), move.dir().turn(Turn.AROUND)));
            final Builder<Move> moves = Stream.builder();
            for (final Direction dir : dirs) {
                Cell cell = move.cell();
                int hl = 0;
                for (int i = 1; i <= maxMoves; i++) {
                    cell = cell.at(dir);
                    if (!grid.isInBounds(cell)) {
                        break;
                    }
                    hl += grid.getValue(cell);
                    if (i >= minMoves) {
                        moves.add(new Move(cell, dir, hl));
                    }
                }
            }
            return moves.build();
        };
        final Cell end = Cell.at(grid.getMaxRowIndex(), grid.getMaxColIndex());
        return (int) Dijkstra.distance(
                new Move(Cell.at(0, 0), null, 0),
                move -> move.cell().equals(end),
                adjacent,
                (curr, next) -> next.cost);
    }

    @Override
    public Integer solvePart1(final IntGrid grid) {
        return solve(grid, 1, 3);
    }
    
    @Override
    public Integer solvePart2(final IntGrid grid) {
        return solve(grid, 4, 10);
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "102"),
        @Sample(method = "part2", input = TEST1, expected = "94"),
        @Sample(method = "part2", input = TEST2, expected = "71"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_17.create().run();
    }

    private static final String TEST1 = """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
            """;
    private static final String TEST2 = """
            111111111111
            999999999991
            999999999991
            999999999991
            999999999991
            """;
}
