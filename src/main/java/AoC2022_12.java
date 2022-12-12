import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.graph.AStar;
import com.github.pareronia.aoc.graph.AStar.Result;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_12 extends AoCBase {
    
    private final Grid grid;
    private final Cell end;
    
    private AoC2022_12(final List<String> input, final boolean debug) {
        super(debug);
        this.grid = Grid.from(input);
        log(this.grid);
        this.end = this.grid.findAllMatching(c -> c == 'E').findFirst().orElseThrow();
    }
    
    public static final AoC2022_12 create(final List<String> input) {
        return new AoC2022_12(input, false);
    }

    public static final AoC2022_12 createDebug(final List<String> input) {
        return new AoC2022_12(input, true);
    }
    
    private char getValue(final Cell cell) {
        final char ch = this.grid.getValueAt(cell);
        if (ch == 'S') {
            return 'a';
        } else if (ch == 'E') {
            return 'z';
        } else {
            return ch;
        }
    }
    
    private Result<Cell> solve(final Cell start) {
        log("start:" + start);
        log("end: " + this.end);
        final Function<Cell, Stream<Cell>> adjacent = cell ->
                this.grid.getCapitalNeighbours(cell)
                        .filter(n -> getValue(n) - getValue(cell) <= 1);
        final Function<Cell, Integer> cost = cell -> 1;
        return AStar.execute(start, cell -> cell.equals(this.end), adjacent, cost);
    }
    
    @Override
    public Integer solvePart1() {
        final Cell start = this.grid.findAllMatching(c -> c == 'S')
                .findFirst().orElseThrow();
        final Result<Cell> result = solve(start);
        return (int) result.getDistance(end);
    }

    @Override
    public Integer solvePart2() {
        return (int) this.grid.findAllMatching(c -> c == 'S' || c == 'a')
            .peek(this::log)
            .map(this::solve)
            .filter(result -> result.getDistances().containsKey(this.end))
            .mapToLong(result -> result.getDistance(this.end))
            .min().orElseThrow();
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_12.createDebug(TEST).solvePart1() == 31;
        assert AoC2022_12.createDebug(TEST).solvePart2() == 29;

        final Puzzle puzzle = Aocd.puzzle(2022, 12);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_12.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_12.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "Sabqponm\r\n" +
        "abcryxxl\r\n" +
        "accszExk\r\n" +
        "acctuvwj\r\n" +
        "abdefghi"
    );
}
