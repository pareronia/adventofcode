import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.navigation.Headings;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2015_18 extends AoCBase {
    
    private static final char ON = '#';
    private static final char OFF = '.';
    
    private final Set<Cell> grid;
    private final int height;
    private final int width;
    
    private AoC2015_18(final List<String> inputs, final boolean debug) {
        super(debug);
        this.height = inputs.size();
        this.width = inputs.get(0).length();
        this.grid = IntStream.range(0, this.height).boxed()
            .flatMap(r -> IntStream.range(0, this.width).mapToObj(c -> Cell.at(r, c)))
            .filter(c -> inputs.get(c.getRow()).charAt(c.getCol()) == ON)
            .collect(toSet());
    }

    public static final AoC2015_18 create(final List<String> input) {
        return new AoC2015_18(input, false);
    }

    public static final AoC2015_18 createDebug(final List<String> input) {
        return new AoC2015_18(input, true);
    }
    
    private final Map<Cell, Set<Cell>> neighboursCache = new HashMap<>();
    private Set<Cell> findNeighbours(final Cell c) {
        if (neighboursCache.containsKey(c)) {
            return neighboursCache.get(c);
        }
        final Set<Cell> neighbours = Headings.OCTANTS.stream()
            .filter(n -> c.getRow() + n.getX() >= 0)
            .filter(n -> c.getRow() + n.getX() < this.height)
            .filter(n -> c.getCol() + n.getY() >= 0)
            .filter(n -> c.getCol() + n.getY() < this.width)
            .map(n -> Cell.at(c.getRow() + n.getX(), c.getCol() + n.getY()))
            .collect(toSet());
        neighboursCache.put(c, neighbours);
        return neighbours;
    }
    
    private void nextGen(final Set<Cell> stuckPositions) {
        final Set<Cell> toOn = new HashSet<>();
        final Set<Cell> toOff = new HashSet<>();
        for (final Cell cell : this.grid) {
            final Set<Cell> neighbours = findNeighbours(cell);
            final int neighbours_on = (int) neighbours.stream().filter(this.grid::contains).count();
            if (neighbours_on == 2 || neighbours_on == 3 || stuckPositions.contains(cell)) {
                toOn.add(cell);
            } else {
                toOff.add(cell);
            }
            neighbours.stream()
                .filter(n -> !this.grid.contains(n))
                .filter(n -> (int) findNeighbours(n).stream().filter(this.grid::contains).count() == 3)
                .forEach(toOn::add);
        }
        for (final Cell cell : toOn) {
            this.grid.add(cell);
        }
        for (final Cell cell : toOff) {
            this.grid.remove(cell);
        }
    }
    
    private void logGrid() {
        if (!this.debug) {
            return;
        }
        for (int r = 0; r < this.height; r++) {
            final char[] row = new char[this.width];
            for (int c = 0; c < this.width; c++) {
                row[c] = this.grid.contains(Cell.at(r, c)) ? ON : OFF;
            }
            log(new String(row));
        }
        log("");
    }
    
    private int solve1(final int generations) {
        logGrid();
        for (int i = 0; i < generations; i++) {
            nextGen(Set.of());
            logGrid();
        }
        return this.grid.size();
    }
    
    @Override
    public Integer solvePart1() {
        return solve1(100);
    }
    
    private int solve2(final int generations) {
        logGrid();
        final Set<Cell> stuckPositions = Set.of(Cell.at(0, 0), Cell.at(this.height - 1, 0),
                                                Cell.at(0, this.width - 1), Cell.at(this.height- 1, this.width - 1));
        for (final Cell stuck : stuckPositions) {
            this.grid.add(stuck);
        }
        for (int i = 0; i < generations; i++) {
            nextGen(stuckPositions);
            logGrid();
        }
        return this.grid.size();
    }
    
    @Override
    public Integer solvePart2() {
        return solve2(100);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2015_18.create(TEST).solve1(4) == 4;
        assert AoC2015_18.create(TEST).solve2(5) == 17;
        
        final Puzzle puzzle = Aocd.puzzle(2015, 18);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2015_18.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2015_18.create(puzzle.getInputData()).solvePart2())
        );
    }
    
    private static final List<String> TEST = splitLines(
            ".#.#.#\r\n" +
            "...##.\r\n" +
            "#....#\r\n" +
            "..#...\r\n" +
            "#.#..#\r\n" +
            "####.."
    );
}
