import static com.github.pareronia.aoc.IterTools.combinations;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.IterTools;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2016_24 extends AoCBase {

    private static final char WALL = '#';
    private static final char OPEN = '.';
    private static final char START = '0';
    private static final int MAX_POIS = 10;  // ?

    private final transient CharGrid grid;
    private final transient Map<Character, Cell> pois;
    private final transient Cell start;

    private AoC2016_24(final List<String> inputs, final boolean debug) {
        super(debug);
        this.grid = CharGrid.from(inputs).getWithEdgesRemoved();
        this.pois = this.grid
                .findAllMatching(c -> !Set.of(WALL, OPEN).contains(c))
                .collect(toMap(c -> this.grid.getValue(c), c -> c));
        this.start = this.pois.get(START);
        log(this.grid);
        log(this.pois);
        log(this.start);
    }

    public static AoC2016_24 create(final List<String> input) {
        return new AoC2016_24(input, false);
    }

    public static AoC2016_24 createDebug(final List<String> input) {
        return new AoC2016_24(input, true);
    }
    
    private List<Path> neighbours(final Path path) {
        return this.grid.getCapitalNeighbours(path.getPosition())
            .filter(cell -> this.grid.getValue(cell) != WALL)
            .map(newPos -> {
                final char value = this.grid.getValue(newPos);
                if (this.pois.containsKey(value)) {
                    return Path.from(path, newPos, value);
                } else {
                    return Path.from(path, newPos);
                }
            })
            .collect(toList());
    }

    private Path findPath(final Character from, final Character to) {
        log(() -> String.format("From %s to %s", from, to));
        final Cell fromCell = this.pois.get(from);
        final Cell toCell = this.pois.get(to);
        final PriorityQueue<Path> paths
                = new PriorityQueue<>(comparing(path -> path.calcScore(toCell)));
        paths.add(new Path(0, fromCell, List.of(from)));
        final Set<Cell> seen = new HashSet<>();
        seen.add(fromCell);
        while (paths.size() < 5_000_000) {
            final Path path = paths.poll();
            if (path.getPosition().equals(toCell)) {
                log(path);
                return path;
            }
            neighbours(path).stream()
                    .filter(p -> !seen.contains(p.getPosition()))
                    .forEach(p -> {
                        paths.add(p);
                        seen.add(p.getPosition());
                    });
        }
        throw new IllegalStateException("Unsolvable");
    }
    
    private Integer findDistance(final boolean backHome) {
        int dist = 0;
        final Set<Character> pois = new TreeSet<>(this.pois.keySet());
        final Set<Path> seen = new HashSet<>();
        final Deque<Path> paths = new ArrayDeque<>();
        paths.add(new Path(0, this.start, List.of(START)));
        while (!paths.isEmpty()) {
            final Path path = paths.poll();
            if (path.getPois().equals(pois) && dist == 0
                    && (!backHome || path.getPosition().equals(this.start))) {
                log(path);
                dist = path.getLength();
            }
            if (seen.contains(path)) {
                continue;
            }
            seen.add(path);
            neighbours(path).stream().forEach(paths::add);
        }
        return dist;
    }
    
    record FromTo(char from, char to) {
        public static FromTo of(final char from, final char to) {
            return new FromTo(from, to);
        }
    }
        
    public Integer solveAlt() {
        final List<Character> poiKeys = List.copyOf(this.pois.keySet());
        final Map<FromTo, Integer> distances = new HashMap<>();
        combinations(poiKeys.size(), 2).stream().forEach(a -> {
            final Character from = poiKeys.get(a[0]);
            final Character to = poiKeys.get(a[1]);
            final Path path = findPath(from, to);
            distances.put(FromTo.of(from, to), path.getLength());
            distances.put(FromTo.of(to, from), path.getLength());
        });
        log(distances);
        final List<Integer> totals = new ArrayList<>();
        final List<Character> poiKeysWithoutStart = poiKeys.stream()
                .filter(c -> c != START)
                .collect(toList());
        IterTools.permutations(poiKeysWithoutStart).forEach(l -> {
            l.add(0, START);
            int total = 0;
            for (int i = 0; i < l.size() - 1; i++) {
                total += distances.get(FromTo.of(l.get(i), l.get(i + 1)));
            }
            totals.add(total);
        });
        return totals.stream().min(naturalOrder()).orElseThrow();
    }
    
    @Override
    public Integer solvePart1() {
        return findDistance(FALSE);
    }
    
    @Override
    public Integer solvePart2() {
        return findDistance(TRUE);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2016_24.createDebug(TEST).solvePart1() == 14;
        assert AoC2016_24.createDebug(TEST).solveAlt() == 14;

        final Puzzle puzzle = Aocd.puzzle(2016, 24);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2016_24.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2016_24.create(inputData)::solvePart2)
        );
        lap("Part 1 alt", AoC2016_24.create(inputData)::solveAlt);
    }
    
    private static final List<String> TEST = splitLines(
            """
                ###########
                #0.1.....2#
                #.#######.#
                #4.......3#
                ###########"""
    );
    
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

        public static Path from(final Path other, final Cell position,
                final Character poi) {
            final List<Character> pois = new ArrayList<>(other.pois);
            pois.add(poi);
            return new Path(other.length + 1, position, pois);
        }
        
        public Set<Character> getPois() {
            return new TreeSet<>(this.pois);
        }
        
        public List<Character> getPoisList() {
            return this.pois;
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

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Path [length=").append(length)
                .append(", position=").append(position).append(", getPois=")
                .append(getPois()).append(", getPoisList=")
                .append(getPoisList()).append(", score=").append(score())
                .append("]");
            return builder.toString();
        }
    }
}
