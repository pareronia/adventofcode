import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.graph.BFS;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_12 extends AoCBase {
    
    private final CharGrid grid;
    private final Cell end;
    
    private AoC2022_12(final List<String> input, final boolean debug) {
        super(debug);
        this.grid = CharGrid.from(input);
        trace(this.grid);
        this.end = this.grid.findAllMatching(c -> c == 'E')
                    .findFirst().orElseThrow();
        trace("end: " + this.end);
    }
    
    public static final AoC2022_12 create(final List<String> input) {
        return new AoC2022_12(input, false);
    }

    public static final AoC2022_12 createDebug(final List<String> input) {
        return new AoC2022_12(input, true);
    }
    
    private char getValue(final Cell cell) {
        final char ch = this.grid.getValue(cell);
        return switch (ch) {
            case 'S' -> 'a';
            case 'E' -> 'z';
            default -> ch;
        };
    }
    
    private int solve(final Set<Character> endPoints) {
        final Predicate<Cell> isEnd = cell ->
                endPoints.contains(this.grid.getValue(cell));
        final Function<Cell, Stream<Cell>> adjacent = cell ->
                this.grid.getCapitalNeighbours(cell)
                        .filter(n -> getValue(cell) - getValue(n) <= 1);
        return BFS.execute(this.end, isEnd, adjacent);
    }
    
    @Override
    public Integer solvePart1() {
        return solve(Set.of('S'));
    }

    @Override
    public Integer solvePart2() {
        return solve(Set.of('S', 'a'));
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

    private static final List<String> TEST = splitLines("""
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi
            """);
}
