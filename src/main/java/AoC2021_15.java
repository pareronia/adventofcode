import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import com.github.pareronia.aoc.IntGrid;
import com.github.pareronia.aoc.IntGrid.Cell;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AoC2021_15 extends AoCBase {
    
    private final IntGrid grid;

    private AoC2021_15(final List<String> input, final boolean debug) {
        super(debug);
        this.grid = IntGrid.from(input);
    }
    
    public static final AoC2021_15 create(final List<String> input) {
        return new AoC2021_15(input, false);
    }

    public static final AoC2021_15 createDebug(final List<String> input) {
        return new AoC2021_15(input, true);
    }
    
    private int findLeastRisk(
            final Cell start,
            final Cell end,
            final Function<Cell, Integer> getRisk,
            final Function<Cell, Stream<Cell>> findNeighbours
        ) {
        final PriorityQueue<State> q = new PriorityQueue<>();
        q.add(new State(start, 0));
        final Set<Cell> seen = new HashSet<>();
        seen.add(start);
        final Map<Cell, Integer> best = new HashMap<>();
        best.put(start, 0);
        while (!q.isEmpty()) {
            final State state = q.poll();
            if (state.getCell().equals(end)) {
                return state.getRisk();
            }
            seen.add(state.getCell());
            final int cTotal = best.getOrDefault(state.getCell(), Integer.MAX_VALUE);
            findNeighbours.apply(state.getCell())
                .filter(n -> !seen.contains(n))
                .forEach(n -> {
                    final int newRisk = cTotal + getRisk.apply(n);
                    if (newRisk < best.getOrDefault(n, Integer.MAX_VALUE)) {
                        best.put(n, newRisk);
                        q.add(new State(n, newRisk));
                    }
            });
        }
        throw new IllegalStateException("Unsolvable");
    }
    
    @RequiredArgsConstructor
    @Getter
    private static final class State implements Comparable<State> {
        private final Cell cell;
        private final int risk;
    
        @Override
        public int compareTo(final State other) {
            return Integer.compare(this.risk, other.risk);
        }
    }
    
    private int getRisk1(final Cell cell) {
        return this.grid.getValue(cell);
    }
    
    private Stream<Cell> findNeighbours1(final Cell c) {
        return Headings.CAPITAL.stream()
            .filter(n -> c.getRow() + n.getX() >= 0)
            .filter(n -> c.getRow() + n.getX() < this.grid.getHeight())
            .filter(n -> c.getCol() + n.getY() >= 0)
            .filter(n -> c.getCol() + n.getY() < this.grid.getWidth())
            .map(n -> Cell.at(c.getRow() + n.getX(), c.getCol() + n.getY()));
    }

    @Override
    public Integer solvePart1() {
        final Cell end = Cell.at(this.grid.getHeight() - 1, this.grid.getWidth() - 1);
        return findLeastRisk(Cell.at(0, 0), end, this::getRisk1, this::findNeighbours1);
    }
    
    private int getRisk2(final Cell cell) {
        final int factorRow = cell.getRow() / this.grid.getHeight();
        final int factorCol = cell.getCol() / this.grid.getWidth();
        final int value = this.grid.getValue(
                Cell.at(cell.getRow() - factorRow * this.grid.getHeight(),
                        cell.getCol() - factorCol * this.grid.getWidth()));
        final int newValue = value + factorCol + factorRow;
        if (newValue > 9) {
            return newValue - 9;
        }
        return newValue;
    }
    
    private Stream<Cell> findNeighbours2(final Cell c) {
        return Headings.CAPITAL.stream()
            .filter(n -> c.getRow() + n.getX() >= 0)
            .filter(n -> c.getRow() + n.getX() < 5 * this.grid.getHeight())
            .filter(n -> c.getCol() + n.getY() >= 0)
            .filter(n -> c.getCol() + n.getY() < 5 * this.grid.getWidth())
            .map(n -> Cell.at(c.getRow() + n.getX(), c.getCol() + n.getY()));
    }

    @Override
    public Integer solvePart2() {
        final Cell end = Cell.at(5 * this.grid.getHeight() - 1, 5 * this.grid.getWidth() - 1);
        return findLeastRisk(Cell.at(0, 0), end, this::getRisk2, this::findNeighbours2);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_15.create(TEST).getRisk1(Cell.at(1, 1)) == 3;
        assert AoC2021_15.create(TEST).getRisk2(Cell.at(1, 1)) == 3;
        assert AoC2021_15.create(TEST).getRisk2(Cell.at(11, 1)) == 4;
        assert AoC2021_15.create(TEST).getRisk2(Cell.at(1, 2)) == 8;
        assert AoC2021_15.create(TEST).getRisk2(Cell.at(1, 22)) == 1;
        assert AoC2021_15.create(TEST).getRisk2(Cell.at(1, 32)) == 2;
        assert AoC2021_15.create(TEST).getRisk2(Cell.at(11, 1)) == 4;
        assert AoC2021_15.create(TEST).solvePart1() == 40;
        assert AoC2021_15.create(TEST).solvePart2() == 315;

        final Puzzle puzzle = Aocd.puzzle(2021, 15);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2021_15.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2021_15.create(puzzle.getInputData()).solvePart2())
        );
    }

    private static final List<String> TEST = splitLines(
        "1163751742\r\n" +
        "1381373672\r\n" +
        "2136511328\r\n" +
        "3694931569\r\n" +
        "7463417111\r\n" +
        "1319128137\r\n" +
        "1359912421\r\n" +
        "3125421639\r\n" +
        "1293138521\r\n" +
        "2311944581"
    );
}