import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.graph.Dijkstra;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2016_13 extends AoCBase {

    private static final char ONE = '1';
    private static final Cell START = Cell.at(1, 1);
    
    private final transient Integer input;
    private final transient Map<Cell, Boolean> openSpaceCache;

    private AoC2016_13(final List<String> inputs, final boolean debug) {
        super(debug);
        assert inputs.size() == 1;
        this.input = Integer.valueOf(inputs.get(0));
        this.openSpaceCache = new HashMap<>();
    }

    public static AoC2016_13 create(final List<String> input) {
        return new AoC2016_13(input, false);
    }

    public static AoC2016_13 createDebug(final List<String> input) {
        return new AoC2016_13(input, true);
    }
    
    private boolean isOpenSpace(final Cell cell) {
        return this.openSpaceCache.computeIfAbsent(cell, pos -> {
            final int x = cell.getRow();
            final int y = cell.getCol();
            final int t = this.input + x * x + 3 * x + 2 * x * y + y + y * y;
            final long ones = Utils.asCharacterStream(Integer.toBinaryString(t))
                    .filter(c -> c == ONE).count();
            return ones % 2 == 0;
        });
    }
    
    private Stream<Cell> adjacent(final Cell c) {
        return c.capitalNeighbours()
            .filter(n -> n.getRow() >= 0)
            .filter(n -> n.getCol() >= 0)
            .filter(this::isOpenSpace);
    }
    
    private Dijkstra.Result<Cell> runAStar() {
        return Dijkstra.execute(
                START,
                cell -> false,
                this::adjacent,
                (curr, next) -> 1);
    }
    
    private int getDistance(final Cell end) {
        return (int) runAStar().getDistance(end);
    }

    @Override
    public Integer solvePart1() {
        return getDistance(Cell.at(31, 39));
    }

    @Override
    public Integer solvePart2() {
        return (int) runAStar()
                .getDistances().values().stream()
                .filter(v -> v <= 50)
                .count();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2016_13.createDebug(TEST).getDistance(Cell.at(1, 1)) == 0;
        assert AoC2016_13.createDebug(TEST).getDistance(Cell.at(7, 4)) == 11;
        
        final Puzzle puzzle = Aocd.puzzle(2016, 13);
        final List<String> input = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", create(input)::solvePart1),
            () -> lap("Part 2", create(input)::solvePart2)
        );
    }
    
    private static final List<String> TEST = splitLines("10");
}