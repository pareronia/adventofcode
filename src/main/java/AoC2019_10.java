import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aocd.Puzzle;

public class AoC2019_10 extends AoCBase {
    
    public static final char ASTEROID = '#';
	
	private final Grid grid;
	
	private AoC2019_10(final List<String> input, final boolean debug) {
		super(debug);
		this.grid = Grid.from(input);
	}
	
	public static AoC2019_10 create(final List<String> input) {
		return new AoC2019_10(input, false);
	}

	public static AoC2019_10 createDebug(final List<String> input) {
		return new AoC2019_10(input, true);
	}
	
	private double angle(final Cell asteroid, final Cell other) {
	    return Math.atan2(
	            other.getRow() - asteroid.getRow(),
	            other.getCol() - asteroid.getCol());
	}
	
	private Set<Double> angles(final Cell asteroid) {
	    return this.grid.getAllEqualTo(ASTEROID)
	        .filter(other -> !other.equals(asteroid))
	        .map(other -> angle(asteroid, other))
	        .collect(toSet());
	}
	
	@Override
	public Integer solvePart1() {
	    return this.grid.getAllEqualTo(ASTEROID)
	        .mapToInt(asteroid -> angles(asteroid).size())
	        .max().getAsInt();
	}
	
	@Override
	public Integer solvePart2() {
	    return 0;
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2019_10.createDebug(TEST1).solvePart1() == 8;
		assert AoC2019_10.createDebug(TEST2).solvePart1() == 33;
		assert AoC2019_10.createDebug(TEST3).solvePart1() == 35;
		assert AoC2019_10.createDebug(TEST4).solvePart1() == 41;
		assert AoC2019_10.createDebug(TEST5).solvePart1() == 210;

		final Puzzle puzzle = Puzzle.create(2019, 10);
		final List<String> input = puzzle.getInputData();
		puzzle.check(
		    () -> lap("Part 1", AoC2019_10.create(input)::solvePart1),
		    () -> lap("Part 2", AoC2019_10.create(input)::solvePart2)
		);
	}
	
	private static final List<String> TEST1 = splitLines(
			".#..#\r\n" +
			".....\r\n" +
			"#####\r\n" +
			"....#\r\n" +
			"...##"
	);
	private static final List<String> TEST2 = splitLines(
	        "......#.#.\r\n" +
	        "#..#.#....\r\n" +
	        "..#######.\r\n" +
	        ".#.#.###..\r\n" +
	        ".#..#.....\r\n" +
	        "..#....#.#\r\n" +
	        "#..#....#.\r\n" +
	        ".##.#..###\r\n" +
	        "##...#..#.\r\n" +
	        ".#....####"
	);
	private static final List<String> TEST3 = splitLines(
	        "#.#...#.#.\r\n" +
            ".###....#.\r\n" +
            ".#....#...\r\n" +
            "##.#.#.#.#\r\n" +
            "....#.#.#.\r\n" +
            ".##..###.#\r\n" +
            "..#...##..\r\n" +
            "..##....##\r\n" +
            "......#...\r\n" +
            ".####.###."
	        );
	private static final List<String> TEST4 = splitLines(
	        ".#..#..###\r\n" +
            "####.###.#\r\n" +
            "....###.#.\r\n" +
            "..###.##.#\r\n" +
            "##.##.#.#.\r\n" +
            "....###..#\r\n" +
            "..#.#..#.#\r\n" +
            "#..#.#.###\r\n" +
            ".##...##.#\r\n" +
            ".....#.#.."
	        );
	private static final List<String> TEST5 = splitLines(
	        ".#..##.###...#######\r\n" +
	        "##.############..##.\r\n" +
	        ".#.######.########.#\r\n" +
	        ".###.#######.####.#.\r\n" +
	        "#####.##.#.##.###.##\r\n" +
	        "..#####..#.#########\r\n" +
	        "####################\r\n" +
	        "#.####....###.#.#.##\r\n" +
	        "##.#################\r\n" +
	        "#####.##.###..####..\r\n" +
	        "..######..##.#######\r\n" +
	        "####.##.####...##..#\r\n" +
	        ".#####..#.######.###\r\n" +
	        "##...#.##########...\r\n" +
	        "#.##########.#######\r\n" +
	        ".####.#.###.###.#.##\r\n" +
	        "....##.##.###..#####\r\n" +
	        ".#.#.###########.###\r\n" +
	        "#.#.#.#####.####.###\r\n" +
	        "###.##.####.##.#..##"
    );
}