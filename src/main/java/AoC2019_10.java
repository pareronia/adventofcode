import static java.util.stream.Collectors.toMap;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Puzzle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AoC2019_10 extends AoCBase {
    
    private static final char ASTEROID = '#';
	
	private final CharGrid grid;
	
	private AoC2019_10(final List<String> input, final boolean debug) {
		super(debug);
		this.grid = CharGrid.from(input);
	}
	
	public static AoC2019_10 create(final List<String> input) {
		return new AoC2019_10(input, false);
	}

	public static AoC2019_10 createDebug(final List<String> input) {
		return new AoC2019_10(input, true);
	}
	
	private Position asPosition(final Cell cell) {
	    return Position.of(cell.getCol(), cell.getRow());
	}
	
	private int distanceSquared(final Position a, final Position b) {
	    final int dx = a.getX() - b.getX();
	    final int dy = a.getY() - b.getY();
	    return dx * dx + dy * dy;
	}
	
	private BinaryOperator<Position> closestTo(final Position pos) {
	    return (a, b) -> distanceSquared(a, pos) < distanceSquared(b, pos) ? a : b;
	}
    
    private double angle(final Position asteroid, final Position other) {
        final double angle = Math.atan2(
                other.getY() - asteroid.getY(),
                other.getX() - asteroid.getX())
            + Math.PI / 2;
        return angle < 0 ? angle + 2 * Math.PI : angle;
    }
	
	private Map<Double, Position> angles(final Position asteroid) {
	    return this.grid.getAllEqualTo(ASTEROID)
	        .map(this::asPosition)
	        .filter(other -> !other.equals(asteroid))
	        .collect(toMap(
	                other -> angle(asteroid, other),
	                Function.identity(),
	                closestTo(asteroid),
	                TreeMap::new));
	}
	
	private Asteroid best() {
	    return this.grid.getAllEqualTo(ASTEROID)
	        .map(this::asPosition)
	        .map(asteroid -> new Asteroid(asteroid, angles(asteroid)))
	        .sorted(Asteroid.byOthersCountDescending())
	        .findFirst().orElseThrow();
	}
	
	@Override
	public Integer solvePart1() {
	    return best().getOthers().size();
	}
	
	@Override
	public Integer solvePart2() {
	    return best().getOthers().values().stream()
	        .skip(199)
	        .limit(1)
	        .findFirst()
	        .map(n200 -> n200.getX() * 100 + n200.getY())
	        .orElseThrow();
	}

	public static void main(final String[] args) throws Exception {
		assert AoC2019_10.createDebug(TEST1).solvePart1() == 8;
		assert AoC2019_10.createDebug(TEST2).solvePart1() == 33;
		assert AoC2019_10.createDebug(TEST3).solvePart1() == 35;
		assert AoC2019_10.createDebug(TEST4).solvePart1() == 41;
		assert AoC2019_10.createDebug(TEST5).solvePart1() == 210;
		assert AoC2019_10.createDebug(TEST5).solvePart2() == 802;

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
	
	@RequiredArgsConstructor
	@Getter
	private static final class Asteroid {
	    private final Position position;
	    private final Map<Double, Position> others;
	    
	    public static Comparator<Asteroid> byOthersCountDescending() {
	        return (a, b) -> Integer.compare(b.getOthers().size(), a.getOthers().size());
	    }
	}
}