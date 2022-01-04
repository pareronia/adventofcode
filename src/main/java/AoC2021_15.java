import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

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
    
    private Stream<Cell> findLeastRiskPath(final int tiles) {
        final Cell start = Cell.at(0,0);
        final Cell end = Cell.at(
                tiles * this.grid.getHeight() - 1,
                tiles * this.grid.getWidth() - 1);
        final PriorityQueue<State> q = new PriorityQueue<>();
        q.add(new State(start, 0));
        final Map<Cell, Integer> best = new HashMap<>();
        best.put(start, 0);
        final Map<Cell, Cell> parent = new HashMap<>();
        while (!q.isEmpty()) {
            final State state = q.poll();
            if (state.getCell().equals(end)) {
                final Builder<Cell> builder = Stream.builder();
                builder.add(end);
                Cell curr = end;
                while (parent.keySet().contains(curr)) {
                    curr = parent.get(curr);
                    builder.add(curr);
                }
                return builder.build();
            }
            final int cTotal = best.getOrDefault(state.getCell(), Integer.MAX_VALUE);
            findNeighbours(state.getCell(), tiles)
                .forEach(n -> {
                    final int newRisk = cTotal + getRisk(n);
                    if (newRisk < best.getOrDefault(n, Integer.MAX_VALUE)) {
                        best.put(n, newRisk);
                        parent.put(n, state.getCell());
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
    
    private int getRisk(final Cell cell) {
        int value = this.grid.getValue(
                        Cell.at(cell.getRow() % this.grid.getHeight(),
                                cell.getCol() % this.grid.getWidth()))
                + cell.getRow() / this.grid.getHeight()
                + cell.getCol() / this.grid.getWidth();
        while (value > 9) {
            value -= 9;
        }
        return value;
    }
    
    private Stream<Cell> findNeighbours(final Cell c, final int tiles) {
        return Headings.CAPITAL.stream()
            .filter(n -> c.getRow() + n.getX() >= 0)
            .filter(n -> c.getRow() + n.getX() < tiles * this.grid.getHeight())
            .filter(n -> c.getCol() + n.getY() >= 0)
            .filter(n -> c.getCol() + n.getY() < tiles * this.grid.getWidth())
            .map(n -> Cell.at(c.getRow() + n.getX(), c.getCol() + n.getY()));
    }
    
    private int solve(final int tiles) {
        return findLeastRiskPath(tiles)
            .filter(c -> !c.equals(Cell.at(0, 0)))
            .mapToInt(this::getRisk)
            .sum();
    }
    
    private void visualize(final int tiles) {
        final Set<Cell> path = findLeastRiskPath(tiles).collect(toSet());
        IntStream.range(0, tiles * this.grid.getHeight()).forEach(row -> {
            final String string = IntStream.range(0, tiles * this.grid.getWidth())
                .mapToObj(col -> {
                    if (path.contains(Cell.at(row, col))) {
                        return '#';
                    } else {
                        return '.';
                    }
                })
                .collect(toAString());
            System.out.println(string);
        });
    }
    
    public void visualizePart1() {
        visualize(1);
    }
    
    public void visualizePart2() {
        visualize(5);
    }

    @Override
    public Integer solvePart1() {
        return solve(1);
    }
    
    @Override
    public Integer solvePart2() {
        return solve(5);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_15.create(TEST).getRisk(Cell.at(1, 1)) == 3;
        assert AoC2021_15.create(TEST).getRisk(Cell.at(1, 1)) == 3;
        assert AoC2021_15.create(TEST).getRisk(Cell.at(11, 1)) == 4;
        assert AoC2021_15.create(TEST).getRisk(Cell.at(1, 2)) == 8;
        assert AoC2021_15.create(TEST).getRisk(Cell.at(1, 22)) == 1;
        assert AoC2021_15.create(TEST).getRisk(Cell.at(1, 32)) == 2;
        assert AoC2021_15.create(TEST).getRisk(Cell.at(11, 1)) == 4;
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