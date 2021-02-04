import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aocd.Aocd;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AoC2020_03 extends AoCBase {
	
	private final Slope slope;

	private AoC2020_03(List<String> input, boolean debug) {
		super(debug);
		this.slope = parse(input);
	}
	
	public static AoC2020_03 create(List<String> input) {
		return new AoC2020_03(input, false);
	}

	public static AoC2020_03 createDebug(List<String> input) {
		return new AoC2020_03(input, true);
	}
	
	private Slope parse(List<String> inputs) {
		final Grid grid = Grid.from(inputs);
		return new Slope(grid.getAllEqualTo('#').collect(toSet()),
						 grid.getWidth(),
						 grid.getHeight());
	}
	
	private List<Cell> path(Steps steps) {
		final List<Cell> path = new ArrayList<>();
		for (int row = 0, col = 0;
				row < slope.getHeight();
				row += steps.getDown(), col = (col + steps.getRight()) % slope.getWidth()) {
			path.add(Cell.at(row, col));
		}
		return path;
	}
	
	private int doRun(Steps steps) {
		return CollectionUtils.intersection(path(steps), slope.trees).size();
	}
	
	@Override
	public long solvePart1() {
		return doRun(Steps.of(1, 3));
	}

	@Override
	public long solvePart2() {
		return Stream.of(	Steps.of(1, 1),
							Steps.of(1, 3),
							Steps.of(1, 5),
							Steps.of(1, 7),
							Steps.of(2, 1))
				.map(this::doRun)
				.reduce(1, (a, b) -> a * b);
	}

	public static void main(String[] args) throws Exception {
		assert AoC2020_03.createDebug(TEST).doRun(Steps.of(1, 1)) == 2;
		assert AoC2020_03.createDebug(TEST).doRun(Steps.of(1, 3)) == 7;
		assert AoC2020_03.createDebug(TEST).doRun(Steps.of(1, 5)) == 3;
		assert AoC2020_03.createDebug(TEST).doRun(Steps.of(1, 7)) == 4;
		assert AoC2020_03.createDebug(TEST).doRun(Steps.of(2, 1)) == 2;
		assert AoC2020_03.createDebug(TEST).solvePart2() == 336;
		
		final List<String> input = Aocd.getData(2020, 3);
		lap("Part 1", () -> AoC2020_03.create(input).solvePart1());
		lap("Part 2", () -> AoC2020_03.create(input).solvePart2());
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
	
	@RequiredArgsConstructor
	@Getter
	private static final class Slope {
		private final Set<Cell> trees;
		private final Integer width;
		private final Integer height;
	}
	
	@RequiredArgsConstructor
	@Getter
	private static final class Steps {
		private final Integer down;
		private final Integer right;
		
		public static Steps of(Integer down, Integer right) {
			return new Steps(down, right);
		}
	}
}
