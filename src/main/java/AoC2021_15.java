import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
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
        grid = IntGrid.from(input);
    }
    
    public static final AoC2021_15 create(final List<String> input) {
        return new AoC2021_15(input, false);
    }

    public static final AoC2021_15 createDebug(final List<String> input) {
        return new AoC2021_15(input, true);
    }
    
    private Stream<Cell> findNeighbours(final Cell c) {
        return Headings.CAPITAL.stream()
            .filter(n -> c.getRow() + n.getX() >= 0)
            .filter(n -> c.getRow() + n.getX() < this.grid.getHeight())
            .filter(n -> c.getCol() + n.getY() >= 0)
            .filter(n -> c.getCol() + n.getY() < this.grid.getWidth())
            .map(n -> Cell.at(c.getRow() + n.getX(), c.getCol() + n.getY()));
    }
    
    private int bfs(final Cell start, final Cell end) {
        final PriorityQueue<State> q = new PriorityQueue<>();
        final Set<Cell> seen = new HashSet<>();
        final Map<Cell, Integer> risks = new HashMap<>();
        risks.put(start, 0);
        seen.add(start);
        q.add(new State(start, 0));
        while (!q.isEmpty()) {
            final State state = q.poll();
            final Cell c = state.getCell();
            if (c.equals(end)) {
                return state.getRisk();
            }
            seen.add(c);
            findNeighbours(c)
                .filter(n -> !seen.contains(n))
                .forEach(n -> {
                    final int newRisk = state.getRisk() + this.grid.getValue(n);
                    final int risk = risks.getOrDefault(n, Integer.MAX_VALUE);
                    if (newRisk < risk) {
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

    @Override
    public Integer solvePart1() {
        return bfs(Cell.at(0, 0), Cell.at(this.grid.getHeight() - 1, this.grid.getWidth() - 1));
    }
    
    @Override
    public Long solvePart2() {
        return null;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2021_15.create(TEST).solvePart1() == 40;
        assert AoC2021_15.create(TEST).solvePart2() == null;

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