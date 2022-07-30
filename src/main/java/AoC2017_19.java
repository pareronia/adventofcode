import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_19 extends AoCBase {
    
    private final Grid grid;
    
    private AoC2017_19(final List<String> inputs, final boolean debug) {
        super(debug);
        this.grid = new Grid(inputs);
    }

    public static AoC2017_19 create(final List<String> input) {
        return new AoC2017_19(input, false);
    }

    public static AoC2017_19 createDebug(final List<String> input) {
        return new AoC2017_19(input, true);
    }
    
    @Override
    public String solvePart1() {
        final StringBuilder ans = new StringBuilder();
        final Cell start = Cell.at(0, this.grid.getRowAsString(0).indexOf('|'));
        log(start);
        final Deque<Cell> seen = new ArrayDeque<>();
        Stream<Cell> stream = this.grid.getCellsS(start);
        while (true) {
            stream
                .peek(c -> log(c))
                .takeWhile(c -> this.grid.getValueAt(c) != ' ')
                .forEach(c -> {
                    seen.addLast(c);
                    final char value = this.grid.getValueAt(c);
                    if (!Set.of('+', '|', '-').contains(value)) {
                        ans.append(value);
                    }
                });
                final Cell last = seen.pollLast();
                final Cell prev = seen.peekLast();
                seen.addLast(last);
                final Optional<Cell> _next = this.grid.getCapitalNeighbours(last)
                        .filter(c -> !c.equals(prev))
                        .filter(c -> this.grid.getValueAt(c) != ' ')
                        .findFirst();
                if (_next.isEmpty()) {
                    break;
                }
                final Cell next = _next.get();
                seen.addLast(next);
                log(next);
                final char value = this.grid.getValueAt(next);
                if (!Set.of('+', '|', '-').contains(value)) {
                    ans.append(value);
                }
                if (next.getCol() < last.getCol()) {
                    stream = this.grid.getCellsW(next);
                } else if (next.getCol() > last.getCol()) {
                    stream = this.grid.getCellsE(next);
                } else if (next.getRow() < last.getRow()) {
                    stream = this.grid.getCellsN(next);
                } else if (next.getRow() > last.getRow()) {
                    stream = this.grid.getCellsS(next);
                } else {
                    throw new IllegalArgumentException();
                }
        }
        log(ans.toString());
        return ans.toString();
    }
    
    @Override
    public String solvePart2() {
        return "";
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_19.createDebug(TEST).solvePart1().equals("ABCDEF");

        final Puzzle puzzle = Aocd.puzzle(2017, 19);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2017_19.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2017_19.create(puzzle.getInputData()).solvePart2())
        );
    }
    
    private static final List<String> TEST = splitLines(
            "     |          \r\n" +
            "     |  +--+    \r\n" +
            "     A  |  C    \r\n" +
            " F---|----E|--+ \r\n" +
            "     |  |  |  D \r\n" +
            "     +B-+  +--+ "
    );
}
