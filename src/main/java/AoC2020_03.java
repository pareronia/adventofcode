import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2020_03 extends AoCBase {
	
	private final Slope slope;

	private AoC2020_03(final List<String> input, final boolean debug) {
		super(debug);
		this.slope = parse(input);
	}
	
	public static AoC2020_03 create(final List<String> input) {
		return new AoC2020_03(input, false);
	}

	public static AoC2020_03 createDebug(final List<String> input) {
		return new AoC2020_03(input, true);
	}
	
	private Slope parse(final List<String> inputs) {
		final CharGrid grid = CharGrid.from(inputs);
		return new Slope(grid.getAllEqualTo('#').collect(toSet()),
						 grid.getWidth(),
						 grid.getHeight());
	}
	
	private List<Cell> path(final Steps steps) {
		final List<Cell> path = new ArrayList<>();
		for (int row = 0, col = 0;
				row < slope.height();
				row += steps.down(), col = (col + steps.right()) % slope.width()) {
			path.add(Cell.at(row, col));
		}
		return path;
	}
	
	private int doRun(final Steps steps) {
	    return (int) path(steps).stream().filter(slope.trees::contains).count();
	}
	
	@Override
	public Integer solvePart1() {
		return doRun(Steps.of(1, 3));
	}

	@Override
	public Long solvePart2() {
		return Stream.of(	Steps.of(1, 1),
							Steps.of(1, 3),
							Steps.of(1, 5),
							Steps.of(1, 7),
							Steps.of(2, 1))
				.map(this::doRun)
				.map(Integer::longValue)
				.reduce(1L, (a, b) -> a * b);
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2020_03.createDebug(TEST).doRun(Steps.of(1, 1)) == 2;
		assert AoC2020_03.createDebug(TEST).doRun(Steps.of(1, 3)) == 7;
		assert AoC2020_03.createDebug(TEST).doRun(Steps.of(1, 5)) == 3;
		assert AoC2020_03.createDebug(TEST).doRun(Steps.of(1, 7)) == 4;
		assert AoC2020_03.createDebug(TEST).doRun(Steps.of(2, 1)) == 2;
		assert AoC2020_03.createDebug(TEST).solvePart2() == 336;
		
        final Puzzle puzzle = Aocd.puzzle(2020, 3);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2020_03.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2020_03.create(inputData)::solvePart2)
        );
	}
	
	private static final List<String> TEST = splitLines(
			"..##.......\r\n" +
			"#...#...#..\r\n" +
			".#....#..#.\r\n" +
			"..#.#...#.#\r\n" +
			".#...##..#.\r\n" +
			"..#.##.....\r\n" +
			".#.#.#....#\r\n" +
			".#........#\r\n" +
			"#.##...#...\r\n" +
			"#...##....#\r\n" +
			".#..#...#.#"
	);
	
	record Slope(Set<Cell> trees, int width, int height) { }
	
	record Steps(int down, int right) {
		
		public static Steps of(final Integer down, final Integer right) {
			return new Steps(down, right);
		}
	}
}
