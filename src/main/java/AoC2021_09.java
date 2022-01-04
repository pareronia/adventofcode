import static java.util.stream.Collectors.summingInt;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.mutable.MutableInt;

import com.github.pareronia.aoc.IntGrid;
import com.github.pareronia.aoc.IntGrid.Cell;
import com.github.pareronia.aoc.navigation.Headings;
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
    
    private Stream<Cell> findNeighbours(final Cell c) {
        return Headings.CAPITAL.stream()
            .filter(n -> c.getRow() + n.getX() >= 0)
            .filter(n -> c.getRow() + n.getX() < this.grid.getHeight())
            .filter(n -> c.getCol() + n.getY() >= 0)
            .filter(n -> c.getCol() + n.getY() < this.grid.getWidth())
            .map(n -> Cell.at(c.getRow() + n.getX(), c.getCol() + n.getY()));
    }

    private Stream<Cell> findLows() {
        return this.grid.getCells()
            .filter(c -> findNeighbours(c).allMatch(
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
        final MutableInt cnt = new MutableInt(0);
        final Set<Cell> seen = new HashSet<>();
        final Deque<Cell> q = new ArrayDeque<>();
        q.add(low);
        while (!q.isEmpty()) {
            final Cell cell = q.poll();
            findNeighbours(cell)
                .filter(n -> !seen.contains(n))
                .filter(n -> this.grid.getValue(n) != 9)
                .forEach(n -> {
                    q.add(n);
                    seen.add(n);
                    cnt.increment();
                });
        }
        return cnt.intValue();
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
