import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import com.github.pareronia.aoc.game_of_life.GameOfLife;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_24
            extends SolutionBase<AoC2020_24.Floor, Integer, Integer> {

    private static final Map<String, Direction> DIRS = Map.of(
         "ne", new Direction(1, -1),
         "nw", new Direction(0, -1),
         "se", new Direction(0, 1),
         "sw", new Direction(-1, 1),
         "e", new Direction(1, 0),
         "w", new Direction(-1, 0)
    );
    
	private AoC2020_24(final boolean debug) {
		super(debug);
	}
	
	public static final AoC2020_24 create() {
		return new AoC2020_24(false);
	}

	public static final AoC2020_24 createDebug() {
		return new AoC2020_24(true);
	}
	
	@Override
    protected AoC2020_24.Floor parseInput(final List<String> inputs) {
        return Floor.fromInput(inputs);
    }

    @Override
	public Integer solvePart1(final Floor floor) {
	    return floor.tiles.size();
	}
	
	@Override
	public Integer solvePart2(final Floor floor) {
	    GameOfLife<Tile> gol
	            = new GameOfLife<>(new HexGrid(), new Rules(), floor.tiles);
        for (int i = 0; i < 100; i++) {
            gol = gol.nextGeneration();
        }
        return gol.alive().size();
	}
	
	@Samples({
	    @Sample(method = "part1", input = TEST, expected = "10"),
	    @Sample(method = "part2", input = TEST, expected = "2208"),
	})
	public static void main(final String[] args) throws Exception {
	    AoC2020_24.create().run();
	}
	
	private static final String TEST = """
	        sesenwnenenewseeswwswswwnenewsewsw
	        neeenesenwnwwswnenewnwwsewnenwseswesw
	        seswneswswsenwwnwse
	        nwnwneseeswswnenewneswwnewseswneseene
	        swweswneswnenwsewnwneneseenw
	        eesenwseswswnenwswnwnwsewwnwsene
	        sewnenenenesenwsewnenwwwse
	        wenwwweseeeweswwwnwwe
	        wsweesenenewnwwnwsenewsenwwsesesenwne
	        neeswseenwwswnwswswnw
	        nenwswwsewswnenenewsenwsenwnesesenew
	        enewnwewneswsewnwswenweswnenwsenwsw
	        sweneswneswneneenwnewenewwneswswnese
	        swwesenesewenwneswnwwneseswwne
	        enesenwswwswneneswsenwnewswseenwsese
	        wnwnesenesenenwwnenwsewesewsesesew
	        nenewswnwewswnenesenwnesewesw
	        eneswnwswnwsenenwnwnwwseeswneewsenese
	        neswnwewnwnwseenwseesewsenwsweewe
	        wseweeenwnesenwwwswn
	        """;
	
	record Tile(int q, int r) {
		
		public static Tile at(final int q, final int r) {
		    return new Tile(q, r);
		}
		
		public Tile at(final Direction direction) {
		    return Tile.at(this.q + direction.q, this.r + direction.r);
		}
	}

	record Direction(int q, int r) {}
	
	record Floor(Set<Tile> tiles) {

        public static Floor fromInput(final List<String> input) {
            final Pattern pattern = Pattern.compile("(n?(e|w)|s?(e|w))");
            final Set<Tile> tiles = new HashSet<>();
            for (final String line : input) {
                final Tile tile = pattern.matcher(line).results()
                    .map(MatchResult::group)
                    .map(DIRS::get)
                    .reduce(Tile.at(0, 0), Tile::at, (t1, t2) -> t2);
                if (tiles.contains(tile)) {
                    tiles.remove(tile);
                } else {
                    tiles.add(tile);
                }
            }
            return new Floor(tiles);
        }
	}
	
	private static final class HexGrid implements GameOfLife.Type<Tile> {

        @Override
        public Map<Tile, Long> getNeighbourCounts(final Set<Tile> alive) {
            return alive.stream()
                .flatMap(tile -> DIRS.values().stream().map(tile::at))
                .collect(groupingBy(tile -> tile, counting()));
        }
	}
	
	private static final class Rules implements GameOfLife.Rules<Tile> {

        @Override
        public boolean alive(final Tile cell, final long cnt, final Set<Tile> alive) {
            return cnt == 2 || (cnt == 1 && alive.contains(cell));
        }
	}
}
