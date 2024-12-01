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
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2019_10
        extends SolutionBase<List<AoC2019_10.Asteroid>, Integer, Integer> {
    
    private static final char ASTEROID = '#';
	
	private AoC2019_10(final boolean debug) {
		super(debug);
	}
	
	public static AoC2019_10 create() {
		return new AoC2019_10(false);
	}

	public static AoC2019_10 createDebug() {
		return new AoC2019_10(true);
	}

	@Override
    protected List<Asteroid> parseInput(final List<String> inputs) {
		final CharGrid grid = CharGrid.from(inputs);
        return grid.getAllEqualTo(ASTEROID)
            .map(asteroid -> Asteroid.from(asteroid, grid))
            .toList();
    }

    private Asteroid best(final List<Asteroid> asteroids) {
	    return asteroids.stream()
	        .sorted(Asteroid.byOthersCountDescending())
	        .findFirst().orElseThrow();
	}
	
	@Override
	public Integer solvePart1(final List<Asteroid> asteroids) {
        return best(asteroids).others().size();
	}
	
	@Override
	public Integer solvePart2(final List<Asteroid> asteroids) {
        return best(asteroids).others().values().stream()
	        .skip(199)
	        .limit(1)
	        .findFirst()
	        .map(n200 -> n200.getX() * 100 + n200.getY())
	        .orElseThrow();
	}

	@Samples({
	    @Sample(method = "part1", input = TEST1, expected = "8"),
	    @Sample(method = "part1", input = TEST2, expected = "33"),
	    @Sample(method = "part1", input = TEST3, expected = "35"),
	    @Sample(method = "part1", input = TEST4, expected = "41"),
	    @Sample(method = "part1", input = TEST5, expected = "210"),
	    @Sample(method = "part2", input = TEST5, expected = "802"),
	})
	public static void main(final String[] args) throws Exception {
	    AoC2019_10.create().run();
	}
	
	private static final String TEST1 = """
                .#..#
                .....
                #####
                ....#
                ...##
                """;
	private static final String TEST2 = """
                ......#.#.
                #..#.#....
                ..#######.
                .#.#.###..
                .#..#.....
                ..#....#.#
                #..#....#.
                .##.#..###
                ##...#..#.
                .#....####
                """;
	private static final String TEST3 = """
                #.#...#.#.
                .###....#.
                .#....#...
                ##.#.#.#.#
                ....#.#.#.
                .##..###.#
                ..#...##..
                ..##....##
                ......#...
                .####.###.
                """;
	private static final String TEST4 = """
                .#..#..###
                ####.###.#
                ....###.#.
                ..###.##.#
                ##.##.#.#.
                ....###..#
                ..#.#..#.#
                #..#.#.###
                .##...##.#
                .....#.#..
                """;
	private static final String TEST5 = """
                .#..##.###...#######
                ##.############..##.
                .#.######.########.#
                .###.#######.####.#.
                #####.##.#.##.###.##
                ..#####..#.#########
                ####################
                #.####....###.#.#.##
                ##.#################
                #####.##.###..####..
                ..######..##.#######
                ####.##.####...##..#
                .#####..#.######.###
                ##...#.##########...
                #.##########.#######
                .####.#.###.###.#.##
                ....##.##.###..#####
                .#.#.###########.###
                #.#.#.#####.####.###
                ###.##.####.##.#..##
                """;
	
	record Asteroid(Position position, Map<Double, Position> others) {
	    
	    public static Asteroid from(final Cell cell, final CharGrid grid) {
	        final Position asteroid = Asteroid.asPosition(cell);
	        return new Asteroid(asteroid, angles(grid, asteroid));
	    }
	    
	    public static Comparator<Asteroid> byOthersCountDescending() {
	        return (a, b) -> Integer.compare(b.others().size(), a.others().size());
	    }
	    
	    private static Position asPosition(final Cell cell) {
	        return Position.of(cell.getCol(), cell.getRow());
	    }
	    
	    private static int distanceSquared(final Position a, final Position b) {
	        final int dx = a.getX() - b.getX();
	        final int dy = a.getY() - b.getY();
	        return dx * dx + dy * dy;
	    }
	    
	    private static BinaryOperator<Position> closestTo(final Position pos) {
	        return (a, b) -> distanceSquared(a, pos) < distanceSquared(b, pos) ? a : b;
	    }
	    
	    private static double angle(final Position asteroid, final Position other) {
	        final double angle = Math.atan2(
	                other.getY() - asteroid.getY(),
	                other.getX() - asteroid.getX())
	            + Math.PI / 2;
	        return angle < 0 ? angle + 2 * Math.PI : angle;
	    }
	    
	    private static Map<Double, Position> angles(
	            final CharGrid grid, final Position asteroid
        ) {
	        return grid.getAllEqualTo(ASTEROID)
	            .map(Asteroid::asPosition)
	            .filter(other -> !other.equals(asteroid))
	            .collect(toMap(
	                    other -> angle(asteroid, other),
	                    Function.identity(),
	                    closestTo(asteroid),
	                    TreeMap::new));
	    }
	}
}