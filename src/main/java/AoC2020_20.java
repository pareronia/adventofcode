import static com.github.pareronia.aoc.SetUtils.union;
import static com.github.pareronia.aoc.StringOps.toBlocks;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Logger;
import com.github.pareronia.aoc.solution.LoggerEnabled;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_20 extends SolutionBase<Set<AoC2020_20.Tile>, Long, Long> {
	
	private AoC2020_20(final boolean debug) {
		super(debug);
	}

	public static final AoC2020_20 create() {
		return new AoC2020_20(false);
	}

	public static final AoC2020_20 createDebug() {
		return new AoC2020_20(true);
	}
	
	@Override
    protected Set<Tile> parseInput(final List<String> inputs) {
		return toBlocks(inputs).stream()
				.map(Tile::fromInput)
				.collect(toSet());
	}
	
	@Override
	public Long solvePart1(final Set<Tile> tiles) {
	    final TileSet tileSet = new TileSet(tiles, this.logger);
		tileSet.getTiles().forEach(this::log);
		return Math.prod(tileSet.findCorners().stream().map(Tile::id).collect(toSet()));
	}
	
	@Override
	public Long solvePart2(final Set<Tile> tiles) {
	    final TileSet ts = new TileSet(new HashSet<>(tiles), this.logger);
	    return ts.findCorners().stream()
	        .map(corner -> {
	            final TileSet tileSet = new TileSet(new HashSet<>(tiles), this.logger);
	            return buildImage(tileSet, corner);
	        })
	        .filter(Optional::isPresent)
	        .map(Optional::get)
	        .peek(this::print)
	        .mapToLong(image -> {
	            final long octothorps = image.countAllEqualTo('#');
	            log("Octothorps: " + octothorps);
	            final List<Cell> nessies = findNessies(image);
	            return octothorps - 15 * nessies.size();
	        })
	        .min().getAsLong();
	}

    private Optional<CharGrid> buildImage(final TileSet tileSet, final Tile corner) {
        tileSet.puzzle(corner);
        tileSet.removeTileEdges();
        tileSet.printPlacedTiles();
        if (Arrays.stream(tileSet.placedTiles).anyMatch(t -> t == null)) {
            return Optional.empty();
        }
        return Optional.of(CharGrid.from(tileSet.createImageGrid()));
    }

    private List<Cell> findNessies(CharGrid image) {
        log("Looking for Nessies...");
		List<Cell> nessies = null;
		final Iterator<CharGrid> permutations = image.getPermutations();
		while (permutations.hasNext()) {
			image = permutations.next();
			nessies = NessieFinder.findNessies(image);
			if (nessies.size() > 1) {
				for (final Cell nessie : nessies) {
					log(String.format("Found 1 Nessie at (%d, %d)!",
							nessie.getRow(), nessie.getCol()));
				}
				break;
			} else if (nessies.size() == 1) {
				final CharGrid grid = NessieFinder.markNessies(nessies, image);
				print(grid);
				log("One is not enough? Looking for more Nessies...");
			}
		}
		image = NessieFinder.markNessies(nessies, image);
		print(image);
        return nessies;
    }

	private void print(final CharGrid image) {
		image.getRowsAsStrings().forEach(this::log);
	}
	
	@Samples({
	    @Sample(method = "part1", input = TEST, expected = "20899048083289"),
	    @Sample(method = "part2", input = TEST, expected = "273"),
	})
	public static void main(final String[] args) throws Exception {
		AoC2020_20.create().run();
	}

	private static final String TEST =
			"""
            Tile 2311:
            ..##.#..#.
            ##..#.....
            #...##..#.
            ####.#...#
            ##.##.###.
            ##...#.###
            .#.#.#..##
            ..#....#..
            ###...#.#.
            ..###..###
            
            Tile 1951:
            #.##...##.
            #.####...#
            .....#..##
            #...######
            .##.#....#
            .###.#####
            ###.##.##.
            .###....#.
            ..#.#..#.#
            #...##.#..
            
            Tile 1171:
            ####...##.
            #..##.#..#
            ##.#..#.#.
            .###.####.
            ..###.####
            .##....##.
            .#...####.
            #.##.####.
            ####..#...
            .....##...
            
            Tile 1427:
            ###.##.#..
            .#..#.##..
            .#.##.#..#
            #.#.#.##.#
            ....#...##
            ...##..##.
            ...#.#####
            .#.####.#.
            ..#..###.#
            ..##.#..#.
            
            Tile 1489:
            ##.#.#....
            ..##...#..
            .##..##...
            ..#...#...
            #####...#.
            #..#.#.#.#
            ...#.#.#..
            ##.#...##.
            ..##.##.##
            ###.##.#..
            
            Tile 2473:
            #....####.
            #..#.##...
            #.##..#...
            ######.#.#
            .#...#.#.#
            .#########
            .###.#..#.
            ########.#
            ##...##.#.
            ..###.#.#.
            
            Tile 2971:
            ..#.#....#
            #...###...
            #.#.###...
            ##.##..#..
            .#####..##
            .#..####.#
            #..#.#..#.
            ..####.###
            ..#.#.###.
            ...#.#.#.#
            
            Tile 2729:
            ...#.#.#.#
            ####.#....
            ..#.#.....
            ....#..#.#
            .##..##.#.
            .#.####...
            ####.#.#..
            ##.####...
            ##..#.##..
            #.##...##.
            
            Tile 3079:
            #.#.#####.
            .#..######
            ..#.......
            ######....
            ####.#..#.
            .#...#.##.
            #.#####.##
            ..#.###...
            ..#.......
            ..#.###...
            """;
	
	record Tile(int id, CharGrid grid) {
		
		public static Tile fromInput(final List<String> block) {
            Integer id = null;
            final List<String> grid = new ArrayList<>();
            for (final String line : block) {
                if (line.startsWith("Tile ")) {
                    id = Integer.valueOf(line.substring("Tile ".length(), line.length() - 1));
                } else {
                    grid.add(line);
                }
            }
            return new Tile(id, CharGrid.from(grid));
		}
		
		private char[] getTopEdge() {
			return this.grid.getTopEdge();
		}

		private char[] getBottomEdge() {
			return this.grid.getBottomEdge();
		}

		private char[] getLeftEdge() {
			return this.grid.getLeftEdge();
		}
		
		private char[] getRightEdge() {
			return this.grid.getRightEdge();
		}
		
		private char[] getRow(final int row)  {
			return this.grid.getRow(row);
		}
		
		private Integer getHeight() {
			return this.grid.getHeight();
		}
		
		private Set<char[]> getAllEdges() {
			return this.grid.getAllEdges();
		}

		private Set<char[]> getAllEdgesReversed() {
			return this.grid.getAllEdgesReversed();
		}
		
		public Tile getWithEdgesRemoved() {
			return new Tile(id, grid.getWithEdgesRemoved());
		}

		public Iterator<Tile> getAllPermutations() {
			return new Iterator<>() {
				final Iterator<CharGrid> inner = Tile.this.grid().getPermutations();
				
				@Override
				public boolean hasNext() {
					return inner.hasNext();
				}

				@Override
				public Tile next() {
					return new Tile(Tile.this.id(), inner.next());
				}
			};
		}
		
		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("Tile ").append(this.id).append(":").append(System.lineSeparator());
//			for (char[] cs : grid) {
//				sb.append(new String(cs)).append(System.lineSeparator());
//			}
			return sb.toString();
		}

		@Override
		public int hashCode() {
			return Objects.hash(id);
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof final Tile other)) {
				return false;
			}
			return Objects.equals(id, other.id);
		}
	}
	
	private static final class TileSet implements LoggerEnabled {
		private final Set<Tile> tiles;
		private final Tile[][] placedTiles;
		private final Logger logger;
	
		public TileSet(final Set<Tile> tiles, final Logger logger) {
			this.tiles = tiles;
			this.logger = logger;
			this.placedTiles = new Tile[Math.sqrt(tiles.size())][Math.sqrt(tiles.size())];
		}

		public Set<Tile> getTiles() {
			return this.tiles;
		}
		
		@Override
        public Logger getLogger() {
            return logger;
        }

        private void placeTile(final Tile tile, final int row, final int col) {
			placedTiles[row][col] = tile;
			tiles.remove(tile);
		}
		
		public void printPlacedTiles() {
			if (placedTiles.length > 3) {
				return;
			}
			for (final Tile[] tiles : placedTiles) {
			    final Tile tile = Arrays.stream(tiles)
			            .filter(Objects::nonNull)
			            .findFirst().orElse(null);
				if (tile == null) {
					continue;
				}
				for (int i = 0; i < tile.getHeight(); i++ ) {
					final int row = i;
					log(Arrays.stream(tiles).map(t -> {
						if (t == null) {
							return "          ";
						}
						return new String(t.getRow(row));
					}).collect(joining(" ")));
				}
				log("");
			}
			for (final Tile[] tiles : placedTiles) {
			    final Tile tile = Arrays.stream(tiles)
			            .filter(Objects::nonNull)
			            .findFirst().orElse(null);
				if (tile == null) {
					continue;
				}
				log(Arrays.stream(tiles).map(t -> {
					if (t == null) {
						return "    ";
					}
					return String.valueOf(t.id());
				}).collect(joining("  ")));
				log("");
			}
		}
		
		private Set<char[]> getEdgesForMatching(final Tile tile) {
		    if (Arrays.stream(placedTiles)
		            .flatMap(Arrays::stream)
		            .anyMatch(tile::equals)) {
				return tile.getAllEdges();
			} else {
				return new HashSet<>(union(tile.getAllEdges(), tile.getAllEdgesReversed()));
			}
		}
		
		private boolean haveCommonEdge(final Tile tile1, final Tile tile2) {
		    return tile1.getAllEdges().stream()
				.flatMap(edge1 -> getEdgesForMatching(tile2).stream()
									.map(edge2 -> new char[][] { edge1, edge2 }))
				.filter(p -> Arrays.equals(p[0], p[1]))
				.count() == 1;
		}
		
		public Set<Tile> findCorners() {
			return getTiles().stream()
				.filter(tile1 -> getTiles().stream()
						.filter(tile2 -> tile2.id() != tile1.id())
						.filter(tile2 -> haveCommonEdge(tile1, tile2))
						.count() == 2)
				.collect(toSet());
		}

		public void puzzle(final Tile corner) {
			log("Unplaced tiles: " + getTiles().size());
			log("Starting with " + corner.id());
			final Iterator<Tile> allPermutations = corner.getAllPermutations();
			while (allPermutations.hasNext()) {
				final Tile next = allPermutations.next();
				placeTile(next, 0, 0);
				final Optional<Tile> right = TileMatcher.findRightSideMatch(next, getTiles());
				final Optional<Tile> bottom = TileMatcher.findBottomSideMatch(next, getTiles());
				if (right.isPresent() && bottom.isPresent()) {
					log("Found corner orientation with right and bottom side match");
					placeTile(right.get(), 0, 1);
					break;
				}
			}
			assert placedTiles[0][0] != null && placedTiles[0][1] != null;
			log("Unplaced tiles: " + getTiles().size());
			printPlacedTiles();
			Tile current = placedTiles[0][1];
			// first row
			log("Continue first row");
			for (int i = 2; i < placedTiles[0].length; i++) {
				current = placedTiles[0][i-1];
				final Optional<Tile> right = TileMatcher.findRightSideMatch(current, getTiles());
				assert right.isPresent();
				placeTile(right.get(), 0, i);
				printPlacedTiles();
				log("Unplaced tiles: " + getTiles().size());
			}
			
			// next rows
			for (int j = 1; j < placedTiles.length; j++) {
				log("Row " + (j + 1));
				for (int i = 0; i < placedTiles[j].length; i++) {
					current = placedTiles[j-1][i];
					final Optional<Tile> bottom = TileMatcher.findBottomSideMatch(current, getTiles());
					assert bottom.isPresent();
					placeTile(bottom.get(), j, i);
					printPlacedTiles();
					log("Unplaced tiles: " + getTiles().size());
				}
			}
		}
		
		public void removeTileEdges() {
			for (int i = 0; i < placedTiles.length; i++) {
				final Tile[] tiles = placedTiles[i];
				for (int j = 0; j < tiles.length; j++) {
					placedTiles[i][j] = tiles[j].getWithEdgesRemoved();
				}
			}
		}
		
		public List<String> createImageGrid() {
			final List<String> grid = new ArrayList<>();
			for (final AoC2020_20.Tile[] tiles : placedTiles) {
				for (int j = 0; j < tiles[0].grid.getHeight(); j++) {
					final StringBuilder row = new StringBuilder();
					for (final Tile tile : tiles) {
						row.append(tile.getRow(j));
					}
					grid.add(row.toString());
				}
			}
			return grid;
		}
	}
	
	private static final class Math {
		
		public static long prod(final Collection<Integer> numbers) {
			return numbers.stream().map(Integer::longValue).reduce(1L, (a, b) -> a * b);
		}
		
		public static int sqrt(final int number) {
			return Double.valueOf(java.lang.Math.sqrt(number)).intValue();
		}
	}
	
	static final class NessieFinder {
		
		private static final Pattern PATTERN2 = Pattern.compile(".\\#..\\#..\\#..\\#..\\#..\\#");
        private static final Pattern PATTERN1 = Pattern.compile("\\#....\\#\\#....\\#\\#....\\#\\#\\#");
        private static final char NESSIE_CHAR = '\u2592';

		public static List<Cell> findNessies(final CharGrid grid) {
			final List<Cell> nessies = new ArrayList<>();
			for (int i = 1; i < grid.getHeight(); i++) {
				final Matcher m1 = PATTERN1.matcher(grid.getRowAsString(i));
				while (m1.find()) {
					final int tail = m1.start(0);
					if ("#".equals(grid.getRowAsString(i - 1).substring(tail + 18, tail + 19))) {
						final Matcher m2 = PATTERN2
								.matcher(grid.getRowAsString(i + 1).substring(tail));
						if (m2.find()) {
							nessies.add(Cell.at(i, tail));
						}
					}
				}
			}
			return nessies;
		}
		
		public static CharGrid markNessies(final List<Cell> nessies, final CharGrid gridIn) {
			final List<String> grid = gridIn.getRowsAsStringList();
			for (final Cell nessie : nessies) {
				final int idx = nessie.getCol();
				final char[] chars0 = grid.get(nessie.getRow() - 1).toCharArray();
				chars0[idx+18] = NESSIE_CHAR;
				grid.set(nessie.getRow() - 1, new String(chars0));
				final char[] chars1 = grid.get(nessie.getRow()).toCharArray();
				chars1[idx] = NESSIE_CHAR;
				chars1[idx+5] = NESSIE_CHAR;
				chars1[idx+6] = NESSIE_CHAR;
				chars1[idx+11] = NESSIE_CHAR;
				chars1[idx+12] = NESSIE_CHAR;
				chars1[idx+17] = NESSIE_CHAR;
				chars1[idx+18] = NESSIE_CHAR;
				chars1[idx+19] = NESSIE_CHAR;
				grid.set(nessie.getRow(), new String(chars1));
				final char[] chars2 = grid.get(nessie.getRow() + 1).toCharArray();
				chars2[idx+1] = NESSIE_CHAR;
				chars2[idx+4] = NESSIE_CHAR;
				chars2[idx+7] = NESSIE_CHAR;
				chars2[idx+10] = NESSIE_CHAR;
				chars2[idx+13] = NESSIE_CHAR;
				chars2[idx+16] = NESSIE_CHAR;
				grid.set(nessie.getRow() + 1, new String(chars2));
			}
			for (int j = 0; j < grid.size(); j++) {
				final char[] chars = grid.get(j).toCharArray();
				for (int i = 0; i < chars.length; i++) {
					if (chars[i] == '#') {
						chars[i] = '~';
					} else if (chars[i] == '.') {
						chars[i] = '_';
					}
				}
				grid.set(j, new String(chars));
			}
			return CharGrid.from(grid);
		}
	}
	
	static final class TileMatcher {
		
		private static Predicate<Tile> rightSide(final Tile tile) {
			return t -> Arrays.equals(tile.getRightEdge(), t.getLeftEdge());
		}
		
		private static Predicate<Tile> bottomSide(final Tile tile) {
			return t -> Arrays.equals(tile.getBottomEdge(), t.getTopEdge());
		}
		
		public static Optional<Tile> findRightSideMatch(final Tile tile, final Set<Tile> tiles) {
			return findMatch(tile, tiles, rightSide(tile));
		}
		
		public static Optional<Tile> findBottomSideMatch(final Tile tile, final Set<Tile> tiles) {
			return findMatch(tile, tiles, bottomSide(tile));
		}
		
		private static Optional<Tile> findMatch(final Tile tile, final Set<Tile> tiles, final Predicate<Tile> matcher) {
			return tiles.stream()
				.flatMap(t -> Utils.stream(t.getAllPermutations()))
				.filter(matcher)
				.findAny();
		}
	}
}
