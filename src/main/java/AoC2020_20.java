import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.CollectionUtils.union;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aocd.Aocd;

public class AoC2020_20 extends AoCBase {
	
	private final TileSet tileSet;
	private final Logger logger;
	
	private AoC2020_20(List<String> input, boolean debug) {
		super(debug);
		this.logger = obj -> log(() -> obj);
		this.tileSet = parse(input);
	}

	public static final AoC2020_20 create(List<String> input) {
		return new AoC2020_20(input, false);
	}

	public static final AoC2020_20 createDebug(List<String> input) {
		return new AoC2020_20(input, true);
	}
	
	private TileSet parse(List<String> inputs) {
		final Set<Tile> tiles = toBlocks(inputs).stream()
				.map(block -> {
					Integer id = null;
					final List<String> grid = new ArrayList<>();
					for (final String line : block) {
						if (line.startsWith("Tile ")) {
							id = Integer.valueOf(line.substring("Tile ".length(), line.length() - 1));
						} else {
							grid.add(line);
						}
					}
					return new Tile(id, grid);
				})
				.collect(toSet());
		return new TileSet(tiles, logger);
	}
	
	@Override
	public Long solvePart1() {
		this.tileSet.getTiles().forEach(this::log);
		return Math.prod(this.tileSet.findCorners().stream().map(Tile::getId).collect(toSet()));
	}
	
	@Override
	public Long solvePart2() {
		this.tileSet.puzzle();
		this.tileSet.removeTileEdges();
		this.tileSet.printPlacedTiles();
		Grid image = Grid.from(this.tileSet.createImageGrid());
		print(image);
		log("Looking for Nessies...");
		List<Pair<Integer, Integer>> nessies = null;
		final Iterator<Grid> permutations = image.getPermutations();
		while (permutations.hasNext()) {
			image = permutations.next();
			nessies = NessieFinder.findNessies(image);
			if (nessies.size() > 1) {
				for (final Pair<Integer, Integer> nessie : nessies) {
					log(String.format("Found 1 Nessie at (%d, %d)!",
							nessie.getLeft(), nessie.getRight()));
				}
				break;
			} else if (nessies.size() == 1) {
				final Grid grid = NessieFinder.markNessies(nessies, image);
				print(grid);
				log("One is not enough? Looking for more Nessies...");
			}
		}
		final long octothorps = image.countAllEqualTo('#');
		log("Octothorps: " + octothorps);
		image = NessieFinder.markNessies(nessies, image);
		print(image);
		return octothorps - 15 * nessies.size();
	}

	private void print(Grid image) {
		image.getRowsAsStrings().forEach(this::log);
	}
	
	public static void main(String[] args) throws Exception {
		assert AoC2020_20.createDebug(splitLines(TEST)).solvePart1() == 20899048083289L;
		assert AoC2020_20.createDebug(splitLines(TEST)).solvePart2() == 273L;
		
		final List<String> input = Aocd.getData(2020, 20);
		lap("Part 1", () -> AoC2020_20.create(input).solvePart1());
		lap("Part 2", () -> AoC2020_20.create(input).solvePart2());
	}

	private static final String TEST =
			"Tile 2311:\r\n" +
			"..##.#..#.\r\n" +
			"##..#.....\r\n" +
			"#...##..#.\r\n" +
			"####.#...#\r\n" +
			"##.##.###.\r\n" +
			"##...#.###\r\n" +
			".#.#.#..##\r\n" +
			"..#....#..\r\n" +
			"###...#.#.\r\n" +
			"..###..###\r\n" +
			"\r\n" +
			"Tile 1951:\r\n" +
			"#.##...##.\r\n" +
			"#.####...#\r\n" +
			".....#..##\r\n" +
			"#...######\r\n" +
			".##.#....#\r\n" +
			".###.#####\r\n" +
			"###.##.##.\r\n" +
			".###....#.\r\n" +
			"..#.#..#.#\r\n" +
			"#...##.#..\r\n" +
			"\r\n" +
			"Tile 1171:\r\n" +
			"####...##.\r\n" +
			"#..##.#..#\r\n" +
			"##.#..#.#.\r\n" +
			".###.####.\r\n" +
			"..###.####\r\n" +
			".##....##.\r\n" +
			".#...####.\r\n" +
			"#.##.####.\r\n" +
			"####..#...\r\n" +
			".....##...\r\n" +
			"\r\n" +
			"Tile 1427:\r\n" +
			"###.##.#..\r\n" +
			".#..#.##..\r\n" +
			".#.##.#..#\r\n" +
			"#.#.#.##.#\r\n" +
			"....#...##\r\n" +
			"...##..##.\r\n" +
			"...#.#####\r\n" +
			".#.####.#.\r\n" +
			"..#..###.#\r\n" +
			"..##.#..#.\r\n" +
			"\r\n" +
			"Tile 1489:\r\n" +
			"##.#.#....\r\n" +
			"..##...#..\r\n" +
			".##..##...\r\n" +
			"..#...#...\r\n" +
			"#####...#.\r\n" +
			"#..#.#.#.#\r\n" +
			"...#.#.#..\r\n" +
			"##.#...##.\r\n" +
			"..##.##.##\r\n" +
			"###.##.#..\r\n" +
			"\r\n" +
			"Tile 2473:\r\n" +
			"#....####.\r\n" +
			"#..#.##...\r\n" +
			"#.##..#...\r\n" +
			"######.#.#\r\n" +
			".#...#.#.#\r\n" +
			".#########\r\n" +
			".###.#..#.\r\n" +
			"########.#\r\n" +
			"##...##.#.\r\n" +
			"..###.#.#.\r\n" +
			"\r\n" +
			"Tile 2971:\r\n" +
			"..#.#....#\r\n" +
			"#...###...\r\n" +
			"#.#.###...\r\n" +
			"##.##..#..\r\n" +
			".#####..##\r\n" +
			".#..####.#\r\n" +
			"#..#.#..#.\r\n" +
			"..####.###\r\n" +
			"..#.#.###.\r\n" +
			"...#.#.#.#\r\n" +
			"\r\n" +
			"Tile 2729:\r\n" +
			"...#.#.#.#\r\n" +
			"####.#....\r\n" +
			"..#.#.....\r\n" +
			"....#..#.#\r\n" +
			".##..##.#.\r\n" +
			".#.####...\r\n" +
			"####.#.#..\r\n" +
			"##.####...\r\n" +
			"##..#.##..\r\n" +
			"#.##...##.\r\n" +
			"\r\n" +
			"Tile 3079:\r\n" +
			"#.#.#####.\r\n" +
			".#..######\r\n" +
			"..#.......\r\n" +
			"######....\r\n" +
			"####.#..#.\r\n" +
			".#...#.##.\r\n" +
			"#.#####.##\r\n" +
			"..#.###...\r\n" +
			"..#.......\r\n" +
			"..#.###...";
	
	static final class Tile {
		private final Integer id;
		private final Grid grid;
		
		public Tile(Integer id, List<String> grid) {
			this.id = id;
			this.grid = Grid.from(grid);
		}
		
		public Tile(Integer id, Grid grid) {
			this.id = id;
			this.grid = grid;
		}
		
		public Integer getId() {
			return id;
		}

		public Grid getGrid() {
			return grid;
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
		
		private char[] getRow(int row)  {
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
			return new Iterator<Tile>() {
				final Iterator<Grid> inner = Tile.this.getGrid().getPermutations();
				
				@Override
				public boolean hasNext() {
					return inner.hasNext();
				}

				@Override
				public Tile next() {
					return new Tile(Tile.this.getId(), inner.next());
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
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof Tile)) {
				return false;
			}
			final Tile other = (Tile) obj;
			return Objects.equals(id, other.id);
		}
	}
	
	private static final class TileSet {
		private final Set<Tile> tiles;
		private final Tile[][] placedTiles;
		private final Logger logger;
	
		public TileSet(Set<Tile> tiles, Logger logger) {
			this.tiles = tiles;
			this.logger = logger;
			this.placedTiles = new Tile[Math.sqrt(tiles.size())][Math.sqrt(tiles.size())];
		}

		public Set<Tile> getTiles() {
			return this.tiles;
		}
		
		private void log(Object object) {
			this.logger.log(object);
		}
		
		private void placeTile(Tile tile, int row, int col) {
			placedTiles[row][col] = tile;
			tiles.remove(tile);
		}
		
		public void printPlacedTiles() {
			if (placedTiles.length > 3) {
				return;
			}
			for (final Tile[] tiles : placedTiles) {
				final Tile tile = ObjectUtils.firstNonNull(tiles);
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
				final Tile tile = ObjectUtils.firstNonNull(tiles);
				if (tile == null) {
					continue;
				}
				log(Arrays.stream(tiles).map(t -> {
					if (t == null) {
						return "    ";
					}
					return String.valueOf(t.getId());
				}).collect(joining("  ")));
				log("");
			}
		}
		
		private Set<char[]> getEdgesForMatching(Tile tile) {
			if (ArrayUtils.contains(placedTiles, tile)) {
				return tile.getAllEdges();
			} else {
				return new HashSet<>(union(tile.getAllEdges(), tile.getAllEdgesReversed()));
			}
		}
		
		private boolean haveCommonEdge(Tile tile1, Tile tile2) {
			return getEdgesForMatching(tile1).stream()
				.flatMap(edge1 -> getEdgesForMatching(tile2).stream()
									.map(edge2 -> Pair.of(edge1, edge2)))
				.filter(p -> Arrays.equals(p.getLeft(), p.getRight()))
				.count() == 2;
		}
		
		public Set<Tile> findCorners() {
			return getTiles().stream()
				.filter(tile1 -> getTiles().stream()
						.filter(tile2 -> !tile2.getId().equals(tile1.getId()))
						.filter(tile2 -> haveCommonEdge(tile1, tile2))
						.count() == 2)
				.collect(toSet());
		}

		public void puzzle() {
			final Set<Tile> corners = findCorners();
			// Pick a corner, any corner...
			final ArrayList<Tile> cornersList = new ArrayList<>(corners);
			Collections.shuffle(cornersList);
			final Tile corner = cornersList.get(0);
			log("Unplaced tiles: " + getTiles().size());
			log("Starting with " + corner.getId());
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
			for (int i = 0; i < placedTiles.length; i++) {
				final Tile[] tiles = placedTiles[i];
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
		
		public static long prod(Collection<Integer> numbers) {
			return numbers.stream().map(Integer::longValue).reduce(1L, (a, b) -> a * b);
		}
		
		public static int sqrt(int number) {
			return Double.valueOf(java.lang.Math.sqrt(number)).intValue();
		}
	}
	
	static final class NessieFinder {
		
		private static final char NESSIE_CHAR = '\u2592';

		public static List<Pair<Integer,Integer>> findNessies(Grid grid) {
			final List<Pair<Integer, Integer>> nessies = new ArrayList<>();
			for (int i = 1; i < grid.getHeight(); i++) {
				final Matcher m1 = Pattern.compile("\\#....\\#\\#....\\#\\#....\\#\\#\\#").matcher(grid.getRowAsString(i));
				while (m1.find()) {
					final int tail = m1.start(0);
					if ("#".equals(grid.getRowAsString(i - 1).substring(tail + 18, tail + 19))) {
						final Matcher m2 = Pattern.compile(".\\#..\\#..\\#..\\#..\\#..\\#")
								.matcher(grid.getRowAsString(i + 1).substring(tail));
						if (m2.find()) {
							nessies.add(Pair.of(i, tail));
						}
					}
				}
			}
			return nessies;
		}
		
		public static Grid markNessies(List<Pair<Integer, Integer>> nessies, Grid gridIn) {
			final List<String> grid = gridIn.getRowsAsStringList();
			for (final Pair<Integer, Integer> nessie : nessies) {
				final int idx = nessie.getRight();
				final char[] chars0 = grid.get(nessie.getLeft() - 1).toCharArray();
				chars0[idx+18] = NESSIE_CHAR;
				grid.set(nessie.getLeft() - 1, new String(chars0));
				final char[] chars1 = grid.get(nessie.getLeft()).toCharArray();
				chars1[idx] = NESSIE_CHAR;
				chars1[idx+5] = NESSIE_CHAR;
				chars1[idx+6] = NESSIE_CHAR;
				chars1[idx+11] = NESSIE_CHAR;
				chars1[idx+12] = NESSIE_CHAR;
				chars1[idx+17] = NESSIE_CHAR;
				chars1[idx+18] = NESSIE_CHAR;
				chars1[idx+19] = NESSIE_CHAR;
				grid.set(nessie.getLeft(), new String(chars1));
				final char[] chars2 = grid.get(nessie.getLeft() + 1).toCharArray();
				chars2[idx+1] = NESSIE_CHAR;
				chars2[idx+4] = NESSIE_CHAR;
				chars2[idx+7] = NESSIE_CHAR;
				chars2[idx+10] = NESSIE_CHAR;
				chars2[idx+13] = NESSIE_CHAR;
				chars2[idx+16] = NESSIE_CHAR;
				grid.set(nessie.getLeft() + 1, new String(chars2));
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
			return Grid.from(grid);
		}
	}
	
	static final class TileMatcher {
		
		private static Predicate<AoC2020_20.Tile> rightSide(Tile tile) {
			return t -> Arrays.equals(tile.getRightEdge(), t.getLeftEdge());
		}
		
		private static Predicate<AoC2020_20.Tile> bottomSide(Tile tile) {
			return t -> Arrays.equals(tile.getBottomEdge(), t.getTopEdge());
		}
		
		public static Optional<Tile> findRightSideMatch(Tile tile, Set<Tile> tiles) {
			return findMatch(tile, tiles, rightSide(tile));
		}
		
		public static Optional<Tile> findBottomSideMatch(Tile tile, Set<Tile> tiles) {
			return findMatch(tile, tiles, bottomSide(tile));
		}
		
		private static Optional<Tile> findMatch(Tile tile, Set<Tile> tiles, Predicate<AoC2020_20.Tile> matcher) {
			return tiles.stream()
				.flatMap(t -> asStream(t.getAllPermutations()))
				.filter(matcher)
				.findAny();
		}
	    
		private static <T> Stream<T> asStream(Iterator<T> sourceIterator) {
	        final Iterable<T> iterable = () -> sourceIterator;
	        return StreamSupport.stream(iterable.spliterator(), false);
	    }
	}
	
	private interface Logger {
		void log(Object object);
	}
}
