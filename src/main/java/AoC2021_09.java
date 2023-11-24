import static java.util.stream.Collectors.summingInt;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import com.github.pareronia.aoc.IntGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.graph.BFS;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2021_09 extends AoCBase {
    
    private final IntGrid grid;
    
    private AoC2021_09(final List<String> input, final boolean debug) {
        super(debug);
        this.grid = IntGrid.from(input);
    }
    
    public static final AoC2021_09 create(final List<String> input) {
        return new AoC2021_09(input, false);
    }

    public static final AoC2021_09 createDebug(final List<String> input) {
        return new AoC2021_09(input, true);
    }

    private Stream<Cell> findLows() {
        return this.grid.getCells()
            .filter(c -> this.grid.getCapitalNeighbours(c).allMatch(
                        n -> this.grid.getValue(n) > this.grid.getValue(c)));
    }
    
    @Override
    public Integer solvePart1() {
        return findLows()
            .map(this.grid::getValue)
            .map(v -> v + 1)
            .collect(summingInt(Integer::intValue));
    }
    
    private int sizeOfBasinAroundLow(final Cell low) {
        final Function<Cell, Stream<Cell>> adjacent =
                cell -> this.grid.getCapitalNeighbours(cell)
                        .filter(n -> this.grid.getValue(n) != 9);
        return BFS.floodFill(low, adjacent).size();
    }

    @Override
    public Integer solvePart2() {
        return findLows()
            .map(this::sizeOfBasinAroundLow)
            .sorted(Comparator.reverseOrder())
            .limit(3)
            .reduce(1, (a, b) -> a * b);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_09.create(TEST).solvePart1() == 15;
        assert AoC2021_09.create(TEST).solvePart2() == 1134;

        final Puzzle puzzle = Aocd.puzzle(2021, 9);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_09.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_09.create(puzzle.getInputData()).solvePart2())
        );
    }

    private static final List<String> TEST = splitLines(
        "2199943210\r\n" +
        "3987894921\r\n" +
        "9856789892\r\n" +
        "8767896789\r\n" +
        "9899965678"
    );
}
