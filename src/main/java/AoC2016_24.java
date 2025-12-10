import static com.github.pareronia.aoc.itertools.IterTools.combinations;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.itertools.IterTools;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;
import com.github.pareronia.aoc.solution.SolutionUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings({"PMD.NoPackage", "PMD.ClassNamingConventions"})
public final class AoC2016_24 extends SolutionBase<AoC2016_24.DuctLayout, Integer, Integer> {

    private static final char WALL = '#';
    private static final char OPEN = '.';
    private static final char START = '0';
    private static final int MAX_POIS = 10; // ?

    private AoC2016_24(final boolean debug) {
        super(debug);
    }

    public static AoC2016_24 create() {
        return new AoC2016_24(false);
    }

    public static AoC2016_24 createDebug() {
        return new AoC2016_24(true);
    }

    @Override
    protected DuctLayout parseInput(final List<String> inputs) {
        return DuctLayout.fromInput(inputs);
    }

    private List<Path> neighbours(final DuctLayout layout, final Path path) {
        return layout.grid
                .getCapitalNeighbours(path.getPosition())
                .filter(cell -> layout.grid.getValue(cell) != WALL)
                .map(
                        newPos -> {
                            final char value = layout.grid.getValue(newPos);
                            if (layout.pois.containsKey(value)) {
                                return Path.from(path, newPos, value);
                            } else {
                                return Path.from(path, newPos);
                            }
                        })
                .collect(toList());
    }

    private Path findPath(final DuctLayout layout, final Character from, final Character to) {
        final Cell fromCell = layout.pois.get(from);
        final Cell toCell = layout.pois.get(to);
        final Queue<Path> paths =
                new PriorityQueue<>(Comparator.comparing(path -> path.calcScore(toCell)));
        paths.add(new Path(0, fromCell, List.of(from)));
        final Set<Cell> seen = new HashSet<>();
        seen.add(fromCell);
        while (paths.size() < 5_000_000) {
            final Path path = paths.poll();
            if (path.getPosition().equals(toCell)) {
                return path;
            }
            neighbours(layout, path).stream()
                    .filter(p -> !seen.contains(p.getPosition()))
                    .forEach(
                            p -> {
                                paths.add(p);
                                seen.add(p.getPosition());
                            });
        }
        throw new IllegalStateException("Unsolvable");
    }

    private Integer findDistance(final DuctLayout layout, final boolean backHome) {
        int dist = 0;
        final Set<Character> pois = new TreeSet<>(layout.pois.keySet());
        final Set<Path> seen = new HashSet<>();
        final Deque<Path> paths = new ArrayDeque<>();
        paths.add(new Path(0, layout.start, List.of(START)));
        while (!paths.isEmpty()) {
            final Path path = paths.poll();
            if (path.getPois().equals(pois)
                    && dist == 0
                    && (!backHome || path.getPosition().equals(layout.start))) {
                dist = path.getLength();
            }
            if (seen.contains(path)) {
                continue;
            }
            seen.add(path);
            neighbours(layout, path).stream().forEach(paths::add);
        }
        return dist;
    }

    record FromTo(char from, char to) {
        public static FromTo of(final char from, final char to) {
            return new FromTo(from, to);
        }
    }

    public int solveAlt(final DuctLayout layout) {
        final List<Character> poiKeys = List.copyOf(layout.pois.keySet());
        final Map<FromTo, Integer> distances = new HashMap<>();
        combinations(poiKeys.size(), 2).stream()
                .forEach(
                        a -> {
                            final Character from = poiKeys.get(a[0]);
                            final Character to = poiKeys.get(a[1]);
                            final Path path = findPath(layout, from, to);
                            distances.put(FromTo.of(from, to), path.getLength());
                            distances.put(FromTo.of(to, from), path.getLength());
                        });
        final List<Integer> totals = new ArrayList<>();
        final List<Character> poiKeysWithoutStart =
                poiKeys.stream().filter(c -> c != START).collect(toList());
        IterTools.permutations(poiKeysWithoutStart)
                .forEach(
                        l -> {
                            l.add(0, START);
                            int total = 0;
                            for (int i = 0; i < l.size() - 1; i++) {
                                total += distances.get(FromTo.of(l.get(i), l.get(i + 1)));
                            }
                            totals.add(total);
                        });
        return totals.stream().mapToInt(Integer::valueOf).min().getAsInt();
    }

    @Override
    public Integer solvePart1(final DuctLayout layout) {
        return findDistance(layout, false);
    }

    @Override
    public Integer solvePart2(final DuctLayout layout) {
        return findDistance(layout, true);
    }

    @Override
    protected void samples() {
        final AoC2016_24 test = createDebug();
        final DuctLayout input = test.parseInput(StringOps.splitLines(TEST));
        assert test.solveAlt(input) == 14;
    }

    @Samples({@Sample(method = "part1", input = TEST, expected = "14")})
    public static void main(final String[] args) throws Exception {
        final AoC2016_24 puzzle = create();
        puzzle.run();
        final DuctLayout input = puzzle.parseInput(puzzle.getInputData());
        SolutionUtils.lap("Part 1 alt", () -> puzzle.solveAlt(input));
    }

    private static final String TEST =
            """
            ###########
            #0.1.....2#
            #.#######.#
            #4.......3#
            ###########
            """;

    private static final class Path {
        private final int length;
        private final Cell position;
        private final List<Character> pois;

        public Path(final int length, final Cell position, final List<Character> pois) {
            this.length = length;
            this.position = position;
            this.pois = new ArrayList<>(pois);
        }

        public static Path from(final Path other, final Cell position) {
            return new Path(other.length + 1, position, other.pois);
        }

        public static Path from(final Path other, final Cell position, final Character poi) {
            final List<Character> pois = new ArrayList<>(other.pois);
            pois.add(poi);
            return new Path(other.length + 1, position, pois);
        }

        public Set<Character> getPois() {
            return new TreeSet<>(this.pois);
        }

        public int calcScore(final Cell destination) {
            return score() * 1_000_000
                    + Math.abs(this.position.getRow() - destination.getRow())
                    + Math.abs(this.position.getCol() - destination.getCol());
        }

        public int score() {
            return this.length * 100 + MAX_POIS - this.pois.size();
        }

        public int getLength() {
            return length;
        }

        public Cell getPosition() {
            return position;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Path other = (Path) obj;
            return Objects.equals(position, other.position)
                    && Objects.equals(getPois(), other.getPois());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getPois(), position);
        }
    }

    record DuctLayout(CharGrid grid, Map<Character, Cell> pois, Cell start) {

        public static DuctLayout fromInput(final List<String> input) {
            final CharGrid grid = CharGrid.from(input).getWithEdgesRemoved();
            final Map<Character, Cell> pois =
                    grid.findAllMatching(c -> !Set.of(WALL, OPEN).contains(c))
                            .collect(toMap(grid::getValue, c -> c));
            final Cell start = pois.get(START);
            return new DuctLayout(grid, pois, start);
        }
    }
}
