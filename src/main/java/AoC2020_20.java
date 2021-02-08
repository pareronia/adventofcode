import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.CollectionUtils.union;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.mutable.MutableLong;
import org.apache.commons.lang3.tuple.Pair;

import com.github.pareronia.aocd.Aocd;

public class AoC2020_20 extends AoCBase {
	
	private final List<String> inputs;
	private final Logger logger;
	
	private AoC2020_20(List<String> input, boolean debug) {
		super(debug);
		this.inputs = input;
		this.logger = obj -> log(() -> obj);
	}

	public static final AoC2020_20 create(List<String> input) {
		return new AoC2020_20(input, false);
	}

	public static final AoC2020_20 createDebug(List<String> input) {
		return new AoC2020_20(input, true);
	}
	
	public Image parse(List<String> inputs) {
		final Set<Tile> tiles = new HashSet<>();
		for (final List<String> block : toBlocks(inputs)) {
			Integer id = null;
			final List<char[]> grid = new ArrayList<>();
			for (final String line : block) {
				if (line.startsWith("Tile ")) {
					id = Integer.valueOf(line.substring("Tile ".length(), line.length() - 1));
				} else {
					grid.add(line.toCharArray());
				}
			}
			tiles.add(new Tile(id, Tile.toGrid(grid)));
		}
		return new Image(tiles, logger);
	}
	
	@Override
	public long solvePart1() {
		final Image image = parse(inputs);
		image.getTiles().forEach(this::log);
		return Math.prod(image.findCorners().stream().map(Tile::getId).collect(toSet()));
	}
	
	@Override
	public long solvePart2() {
		final Image image = parse(inputs);
		image.puzzle();
		image.removeTileEdges();
		image.printPlacedTiles();
		image.createImageGrid();
		image.print();
		log("Looking for Nessies...");
		List<Pair<Integer, Integer>> nessies = null;
		for (int i = 0; i < 8; i++) {
			if (i == 4) {
				image.flipHorizontally();
				log("Flipping");
			} else if (i > 0)  {
				image.rotate();
				log("Rotating");
			}
			nessies = NessieFinder.findNessies(image.grid);
			if (nessies.size() > 1) {
				for (final Pair<Integer, Integer> nessie : nessies) {
					log(String.format("Found 1 Nessie at (%d, %d)!",
							nessie.getLeft(), nessie.getRight()));
				}
				break;
			} else if (nessies.size() == 1) {
				final List<String> grid = image.grid
						.stream().map(s -> new String(s))
						.collect(toList());
				NessieFinder.markNessies(nessies, grid);
				for (final String row : grid) {
					log(row);
				}
				log("One is not enough? Looking for more Nessies...");
			}
		}
		final long octothorps = image.countOctothorps();
		log("Octothorps: " + octothorps);
		NessieFinder.markNessies(nessies, image.grid);
		image.print();
		return octothorps - 15 * nessies.size();
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
		private final char[][] grid;
		
		public static char[][] toGrid(List<char[]> strings) {
			return strings.toArray(new char[strings.size()][]);
		}
		
		public Tile(Integer id, char[][] grid) {
			this.id = id;
			this.grid = grid;
		}
		
		public Integer getId() {
			return id;
		}

		public char[][] getGrid() {
			return grid;
		}

		private Edge getTopEdge() {
			return new Edge(getRow(0));
		}

		private Edge getTopEdgeReversed() {
			return new Edge(new StringBuilder(getTopEdge().getPixels()).reverse().toString());
		}

		private Edge getBottomEdge() {
			return new Edge(getRow(grid.length-1));
		}

		private Edge getBottomEdgeReversed() {
			return new Edge(new StringBuilder(getBottomEdge().getPixels()).reverse().toString());
		}
		
		private Edge getLeftEdge() {
			return new Edge(getColumn(0));
		}
		
		private Edge getLeftEdgeReversed() {
			return new Edge(new StringBuilder(getLeftEdge().getPixels()).reverse().toString());
		}
		
		private Edge getRightEdge() {
			return new Edge(getColumn(grid.length-1));
		}
		
		private Edge getRightEdgeReversed() {
			return new Edge(new StringBuilder(getRightEdge().getPixels()).reverse().toString());
		}
		
		private String getColumn(int col) {
			final char [] column = new char[grid.length];
			for (int i = 0; i < grid[0].length; i++) {
				column[i] = grid[i][col];
			}
			return new String(column);
		}
		
		private String getColumnReversed(int col) {
			return new StringBuilder(getColumn(col)).reverse().toString();
		}
		
		private String getRow(int row)  {
			return new String(grid[row]);
		}
		
		private List<String> getRows() {
			return Stream.of(grid).map(String::valueOf).collect(toList());
		}
		
		public Set<Edge> getAllEdges() {
			final HashSet<Edge> set = new HashSet<>();
			set.add(getTopEdge());
			set.add(getBottomEdge());
			set.add(getLeftEdge());
			set.add(getRightEdge());
			return set;
		}

		public Set<Edge> getAllEdgesReversed() {
			final HashSet<Edge> set = new HashSet<>();
			set.add(getTopEdgeReversed());
			set.add(getBottomEdgeReversed());
			set.add(getLeftEdgeReversed());
			set.add(getRightEdgeReversed());
			return set;
		}
		
		public Tile getFlippedHorizontally() {
			final List<char[]> newGrid = new ArrayList<>();
	        final ListIterator<String> iterator = getRows().listIterator(grid.length);
	        while (iterator.hasPrevious()) {
	        	newGrid.add(iterator.previous().toCharArray());
	        }
	        return new Tile(id, Tile.toGrid(newGrid));
		}
		
		public Tile getRotated() {
			final List<char[]> newGrid = new ArrayList<>();
			for (int i = 0; i < grid.length; i++) {
				newGrid.add(getColumnReversed(i).toCharArray());
			}
			return new Tile(id, Tile.toGrid(newGrid));
		}
		
		public Tile getWithEdgesRemoved() {
			final List<char[]> newGrid = new ArrayList<>();
			for (int i = 1; i < grid.length - 1; i++) {
				final String row = getRow(i);
				newGrid.add(row.substring(1, row.length() - 1).toCharArray());
			}
			return new Tile(id, Tile.toGrid(newGrid));
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
		
		private static final class Edge {
			private final String pixels;
			
			public Edge(String pixels) {
				this.pixels = pixels;
			}
			
			public String getPixels() {
				return pixels;
			}
			
			@Override
			public int hashCode() {
				return Objects.hash(pixels);
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				}
				if (!(obj instanceof Edge)) {
					return false;
				}
				final Edge other = (Edge) obj;
				return Objects.equals(this.pixels, other.pixels);
			}
		}
	}
	
	private static final class Image {
		private final Set<Tile> tiles;
		private final Tile[][] placedTiles;
		private final List<String> grid;
		private final Logger logger;

		public Image(Set<Tile> tiles, Logger logger) {
			this.tiles = tiles;
			this.logger = logger;
			this.placedTiles = new Tile[Math.sqrt(tiles.size())][Math.sqrt(tiles.size())];
			this.grid = new ArrayList<>();
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
		
		private Set<Tile.Edge> getEdgesForMatching(Tile tile) {
			if (ArrayUtils.contains(placedTiles, tile)) {
				return tile.getAllEdges();
			} else {
				return new HashSet<>(union(tile.getAllEdges(), tile.getAllEdgesReversed()));
			}
		}
		
		private Set<Pair<Tile.Edge, Tile.Edge>> getCommonEdges(Tile tile1, Tile tile2) {
			return getEdgesForMatching(tile1).stream()
					.flatMap(tile1Edge -> getEdgesForMatching(tile2).stream()
											.filter(tile2Edge -> tile2Edge.equals(tile1Edge))
											.map(tile2Edge -> Pair.of(tile1Edge, tile2Edge)))
					.collect(toSet());
		}
		
		private Map<Tile, Map<Tile, Set<Pair<Tile.Edge, Tile.Edge>>>> getAllCommonEdges() {
			final Map<Tile, Map<Tile, Set<Pair<Tile.Edge, Tile.Edge>>>> commons = new HashMap<>();
			for (final Tile tile1 : getTiles()) {
				commons.put(tile1, new HashMap<Tile, Set<Pair<Tile.Edge, Tile.Edge>>>());
				for (final Tile tile2 : getTiles()) {
					if (tile1.getId() == tile2.getId()) {
						continue;
					}
					final Set<Pair<Tile.Edge, Tile.Edge>> common = getCommonEdges(tile1, tile2);
					if (!common.isEmpty()) {
						commons.get(tile1).put(tile2, common);
					}
				}
			}
			return commons;
		}
		
		public Set<Tile> findCorners() {
			return getAllCommonEdges().entrySet().stream()
					.filter(e -> e.getValue().size() == 2)
					.map(Map.Entry::getKey)
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
			final Iterator<Tile> allPermutations = TilePermutator.getAllPermutations(corner);
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
		
		public void createImageGrid() {
			for (int i = 0; i < placedTiles.length; i++) {
				final Tile[] tiles = placedTiles[i];
				for (int j = 0; j < tiles[0].grid.length; j++) {
					final StringBuilder row = new StringBuilder();
					for (final Tile tile : tiles) {
						row.append(tile.getRow(j));
					}
					this.grid.add(row.toString());
				}
			}
		}
		
		public void rotate() {
			final ArrayList<String> newGrid = new ArrayList<>();
			for (int i = 0; i < this.grid.get(0).length(); i++) {
				final StringBuilder row = new StringBuilder();
				for (int j = this.grid.size() - 1; j >= 0; j--) {
					row.append(this.grid.get(j).charAt(i));
				}
				newGrid.add(row.toString());
			}
			this.grid.clear();
			this.grid.addAll(newGrid);
		}
		
		public long countOctothorps() {
			return this.grid.stream().flatMapToInt(s -> s.chars()).filter(c -> c == '#').count();
		}
		
		public void flipHorizontally() {
			final ArrayList<String> newGrid = new ArrayList<>();
			for (int j = this.grid.size() - 1; j >= 0; j--) {
				newGrid.add(this.grid.get(j));
			}
			this.grid.clear();
			this.grid.addAll(newGrid);
		}
		
		public void print() {
			for (final String row : this.grid) {
				log(row);
			}
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
				for (int i = 0; i < tile.getRows().size(); i++ ) {
					final int row = i;
					log(Arrays.stream(tiles).map(t -> {
						if (t == null) {
							return "          ";
						}
						return t.getRow(row);
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
	}
	
	private static final class Math {
		
		public static long prod(Collection<Integer> numbers) {
			if (CollectionUtils.isEmpty(numbers)) {
				return 0L;
			}
			final MutableLong result = new MutableLong(1L);
			numbers.forEach(n -> result.setValue(result.longValue() * n));
			return result.longValue();
		}
		
		public static int sqrt(int number) {
			return Double.valueOf(java.lang.Math.sqrt(number)).intValue();
		}
	}
	
	static final class NessieFinder {
		
		private static final char NESSIE_CHAR = '\u2592';

		public static List<Pair<Integer,Integer>> findNessies(List<String> grid) {
			final List<Pair<Integer, Integer>> nessies = new ArrayList<>();
			for (int i = 1; i < grid.size(); i++) {
				final Matcher m1 = Pattern.compile("\\#....\\#\\#....\\#\\#....\\#\\#\\#").matcher(grid.get(i));
				while (m1.find()) {
					final int tail = m1.start(0);
					if ("#".equals(grid.get(i - 1).substring(tail + 18, tail + 19))) {
						final Matcher m2 = Pattern.compile(".\\#..\\#..\\#..\\#..\\#..\\#")
								.matcher(grid.get(i + 1).substring(tail));
						if (m2.find()) {
							nessies.add(Pair.of(i, tail));
						}
					}
				}
			}
			return nessies;
		}
		
		public static void markNessies(List<Pair<Integer, Integer>> nessies, List<String> grid) {
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
		}
	}
	
	static final class TilePermutator {
		
		public static Iterator<Tile> getAllPermutations(Tile in) {
			return new Iterator<Tile>() {
				int i = 0;
				Tile tile = in;
				
				@Override
				public boolean hasNext() {
					return i < 8;
				}

				@Override
				public Tile next() {
					if (i == 4) {
						tile = tile.getFlippedHorizontally();
					} else if (i > 0)  {
						tile = tile.getRotated();
					}
					i++;
					return tile;
				}
			};
		}
	}
	
	static final class TileMatcher {
		
		private static Predicate<AoC2020_20.Tile> rightSide(Tile tile) {
			return t -> tile.getRightEdge().equals(t.getLeftEdge());
		}
		
		private static Predicate<AoC2020_20.Tile> bottomSide(Tile tile) {
			return t -> tile.getBottomEdge().equals(t.getTopEdge());
		}
		
		public static Optional<Tile> findRightSideMatch(Tile tile, Set<Tile> tiles) {
			return findMatch(tile, tiles, rightSide(tile));
		}
		
		public static Optional<Tile> findBottomSideMatch(Tile tile, Set<Tile> tiles) {
			return findMatch(tile, tiles, bottomSide(tile));
		}
		
		private static Optional<Tile> findMatch(Tile tile, Set<Tile> tiles, Predicate<AoC2020_20.Tile> matcher) {
			return tiles.stream()
				.flatMap(t -> asStream(TilePermutator.getAllPermutations(t)))
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
