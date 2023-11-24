import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import com.github.pareronia.aoc.game_of_life.GameOfLife;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class AoC2020_24 extends AoCBase {

    private static final Map<String, Direction> DIRS = Map.of(
         "ne", new Direction(1, -1),
         "nw", new Direction(0, -1),
         "se", new Direction(0, 1),
         "sw", new Direction(-1, 1),
         "e", new Direction(1, 0),
         "w", new Direction(-1, 0)
    );
    
	private final List<String> input;
	
	private AoC2020_24(final List<String> input, final boolean debug) {
		super(debug);
		this.input = input;
	}
	
	public static final AoC2020_24 create(final List<String> input) {
		return new AoC2020_24(input, false);
	}

	public static final AoC2020_24 createDebug(final List<String> input) {
		return new AoC2020_24(input, true);
	}
	
	private Set<Tile> buildFloor() {
	    final Pattern pattern = Pattern.compile("(n?(e|w)|s?(e|w))");
	    final Set<Tile> tiles = new HashSet<>();
	    for (final String line : this.input) {
	        final List<Direction> directions = pattern.matcher(line).results()
	            .map(MatchResult::group)
	            .map(DIRS::get)
	            .collect(toList());
	        Tile tile = Tile.at(0, 0);
	        for (final Direction direction : directions) {
                tile = Tile.at(tile.q + direction.q, tile.r + direction.r);
            }
	        if (tiles.contains(tile)) {
	            tiles.remove(tile);
	        } else {
	            tiles.add(tile);
	        }
	    }
        return tiles;
	}
	
	@Override
	public Integer solvePart1() {
	    return buildFloor().size();
	}
	
	@Override
	public Integer solvePart2() {
	    GameOfLife<Tile> gol = new GameOfLife<>(new HexGrid(), new Rules(), buildFloor());
        for (int i = 0; i < 100; i++) {
            gol = gol.nextGeneration();
        }
        return gol.getAlive().size();
	}
	
	public static void main(final String[] args) throws Exception {
		assert AoC2020_24.createDebug(TEST).solvePart1() == 10;
		assert AoC2020_24.createDebug(TEST).solvePart2() == 2208;
		
        final Puzzle puzzle = Puzzle.create(2020, 24);
        final List<String> input = puzzle.getInputData();
        puzzle.check(
           () -> lap("Part 1", AoC2020_24.create(input)::solvePart1),
           () -> lap("Part 2", AoC2020_24.create(input)::solvePart2)
	    );
	}
	
	private static final List<String> TEST = splitLines(
			"sesenwnenenewseeswwswswwnenewsewsw\r\n" +
			"neeenesenwnwwswnenewnwwsewnenwseswesw\r\n" +
			"seswneswswsenwwnwse\r\n" +
			"nwnwneseeswswnenewneswwnewseswneseene\r\n" +
			"swweswneswnenwsewnwneneseenw\r\n" +
			"eesenwseswswnenwswnwnwsewwnwsene\r\n" +
			"sewnenenenesenwsewnenwwwse\r\n" +
			"wenwwweseeeweswwwnwwe\r\n" +
			"wsweesenenewnwwnwsenewsenwwsesesenwne\r\n" +
			"neeswseenwwswnwswswnw\r\n" +
			"nenwswwsewswnenenewsenwsenwnesesenew\r\n" +
			"enewnwewneswsewnwswenweswnenwsenwsw\r\n" +
			"sweneswneswneneenwnewenewwneswswnese\r\n" +
			"swwesenesewenwneswnwwneseswwne\r\n" +
			"enesenwswwswneneswsenwnewswseenwsese\r\n" +
			"wnwnesenesenenwwnenwsewesewsesesew\r\n" +
			"nenewswnwewswnenesenwnesewesw\r\n" +
			"eneswnwswnwsenenwnwnwwseeswneewsenese\r\n" +
			"neswnwewnwnwseenwseesewsenwsweewe\r\n" +
			"wseweeenwnesenwwwswnew"
	);
	
	@RequiredArgsConstructor(staticName = "at")
	@EqualsAndHashCode
	@ToString
	private static final class Tile {
		private final int q;
		private final int r;
	}

	@RequiredArgsConstructor
	@ToString
	private static final class Direction {
	    private final int q;
	    private final int r;
	}
	
	private static final class HexGrid implements GameOfLife.Type<Tile> {

        @Override
        public Map<Tile, Long> getNeighbourCounts(final Set<Tile> alive) {
	        final Map<Tile, Long> neighbourCounts = new HashMap<>();
	        for (final Tile tile : alive) {
	            for (final Direction d : DIRS.values()) {
	                final Tile n = Tile.at(tile.q + d.q, tile.r + d.r);
	                neighbourCounts.merge(n, 1L, Long::sum);
	            }
	        }
            return neighbourCounts;
        }
	}
	
	private static final class Rules implements GameOfLife.Rules<Tile> {

        @Override
        public boolean alive(final Tile cell, final long cnt, final Set<Tile> alive) {
            return cnt == 2 || (cnt == 1 && alive.contains(cell));
        }
	}
}
