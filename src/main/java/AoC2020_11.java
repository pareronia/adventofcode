import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.RequiredArgsConstructor;

public class AoC2020_11 extends AoCBase {
	
    private static final char FLOOR = '.';
    private static final char EMPTY = 'L';
    private static final char OCCUPIED = '#';

	private final Grid grid;
	private final Map<Cell, Set<Cell>> adjacentCache;
	private final Map<Cell, Set<Cell>> visibleCache;
	
	private AoC2020_11(final List<String> input, final boolean debug) {
		super(debug);
		this.grid = Grid.from(input);
		this.adjacentCache = new HashMap<>();
		this.visibleCache = new HashMap<>();
	}
	
	public static AoC2020_11 create(final List<String> input) {
		return new AoC2020_11(input, false);
	}

	public static AoC2020_11 createDebug(final List<String> input) {
		return new AoC2020_11(input, true);
	}
	
	private Stream<Cell> adjacent(final Cell cell) {
	    return this.adjacentCache.computeIfAbsent(cell, this::findAdjacent).stream();
	}
	
	private Set<Cell> findAdjacent(final Cell cell) {
	    return IntStream.rangeClosed(cell.getRow() - 1, cell.getRow() + 1)
	            .filter(rr -> 0 <= rr && rr < this.grid.getHeight())
	            .boxed()
	            .flatMap(rr -> IntStream.rangeClosed(cell.getCol() - 1, cell.getCol() + 1)
	                            .filter(cc -> 0 <= cc && cc < this.grid.getWidth())
	                            .mapToObj(cc -> Cell.at(rr, cc)))
	            .filter(c -> !c.equals(cell))
	            .collect(toSet());
	}
	
	private Stream<Cell> visible(final Cell cell) {
	    return this.visibleCache.computeIfAbsent(cell, this::findVisible).stream();
	}
	
	private Set<Cell> findVisible(final Cell cell) {
	    final Set<Cell> cells = new HashSet<>();
	    for (final Stream<Cell> stream : Set.of(
	            this.grid.getCellsN(cell),
	            this.grid.getCellsNE(cell),
	            this.grid.getCellsE(cell),
	            this.grid.getCellsSE(cell),
	            this.grid.getCellsS(cell),
	            this.grid.getCellsSW(cell),
	            this.grid.getCellsW(cell),
	            this.grid.getCellsNW(cell)
	    )) {
	        stream.filter(c -> this.grid.getValueAt(c) != FLOOR).findFirst().ifPresent(cells::add);
	    }
	    return cells;
	}
	
	private long countOccupied(final Grid grid, final Cell cell, final Function<Cell, Stream<Cell>> strategy) {
	    return strategy.apply(cell).filter(c -> grid.getValueAt(c) == OCCUPIED).count();
	}
	
	private CycleResult runCycle(final Grid grid, final Function<Cell, Stream<Cell>> strategy, final int tolerance) {
	    final char[][] newGrid = new char[grid.getHeight()][grid.getWidth()];
	    boolean changed = false;
	    for (int rr = 0; rr < grid.getHeight(); rr++) {
	        final char[] newRow = new char[grid.getWidth()];
	        for (int cc = 0; cc < grid.getWidth(); cc++) {
	            final Cell cell = Cell.at(rr, cc);
                final char value = grid.getValueAt(cell);
                if (value == EMPTY && countOccupied(grid, cell, strategy) == 0) {
                    newRow[cc] = OCCUPIED;
                    changed = true;
	            } else if (value == OCCUPIED && countOccupied(grid, cell, strategy) >= tolerance) {
	                newRow[cc] = EMPTY;
	                changed = true;
	            } else  {
	                newRow[cc] = value;
	            }
	        }
	        newGrid[rr] = newRow;
	    }
	    return new CycleResult(new Grid(newGrid), changed);
	}
	
	private int findCountOfEquilibrium(final Function<Cell, Stream<Cell>> strategy, final int tolerance) {
	    Grid grid = this.grid;
	    while (true) {
	        final CycleResult result = runCycle(grid, strategy, tolerance);
	        if (!result.changed) {
	            return (int) result.grid.countAllEqualTo(OCCUPIED);
	        }
	        grid = result.grid;
	    }
	}
	
	@Override
	public Integer solvePart1() {
	    return findCountOfEquilibrium(this::adjacent, 4);
	}
	
	@Override
	public Integer solvePart2() {
	    return findCountOfEquilibrium(this::visible, 5);
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2020_11.createDebug(TEST).solvePart1() == 37;
		assert AoC2020_11.createDebug(TEST).solvePart2() == 26;

        final Puzzle puzzle = Aocd.puzzle(2020, 11);
        puzzle.check(
            () -> lap("Part 1", () -> AoC2020_11.create(puzzle.getInputData()).solvePart1()),
            () -> lap("Part 2", () -> AoC2020_11.create(puzzle.getInputData()).solvePart2())
        );
	}
	
	private static final List<String> TEST = splitLines(
	        "L.LL.LL.LL\r\n" +
	        "LLLLLLL.LL\r\n" +
	        "L.L.L..L..\r\n" +
	        "LLLL.LL.LL\r\n" +
	        "L.LL.LL.LL\r\n" +
	        "L.LLLLL.LL\r\n" +
	        "..L.L.....\r\n" +
	        "LLLLLLLLLL\r\n" +
	        "L.LLLLLL.L\r\n" +
	        "L.LLLLL.LL"
	);
	
	@RequiredArgsConstructor
	private static final class CycleResult {
	    private final Grid grid;
	    private final boolean changed;
	}
}