import static java.util.stream.Collectors.toMap;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aoc.codec.MD5;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@SuppressWarnings({"PMD.NoPackage", "PMD.ClassNamingConventions"})
public final class AoC2016_17 extends SolutionBase<List<AoC2016_17.Path>, String, Integer> {

    private AoC2016_17(final boolean debug) {
        super(debug);
    }

    public static AoC2016_17 create() {
        return new AoC2016_17(false);
    }

    public static AoC2016_17 createDebug() {
        return new AoC2016_17(true);
    }

    @Override
    protected List<Path> parseInput(final List<String> inputs) {
        return new PathFinder(inputs.get(0)).findPaths();
    }

    @Override
    public String solvePart1(final List<Path> paths) {
        return paths.stream().findFirst().map(Path::path).orElseThrow();
    }

    @Override
    public Integer solvePart2(final List<Path> paths) {
        return paths.stream().mapToInt(Path::length).max().getAsInt();
    }

    @Samples({
        @Sample(method = "part1", input = "ihgpwlah", expected = "DDRRRD"),
        @Sample(method = "part1", input = "kglvqrro", expected = "DDUDRLRRUDRD"),
        @Sample(method = "part1", input = "ulqzkmiv", expected = "DRURDRUDDLLDLUURRDULRLDUUDDDRR"),
        @Sample(method = "part2", input = "ihgpwlah", expected = "370"),
        @Sample(method = "part2", input = "kglvqrro", expected = "492"),
        @Sample(method = "part2", input = "ulqzkmiv", expected = "830"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    record Path(String path, Cell position) {

        public int length() {
            return this.path.length();
        }

        public boolean isAt(final Cell position) {
            return this.position.equals(position);
        }
    }

    record PathFinder(String salt) {
        private static final Cell START = Cell.at(0, 0);
        private static final Cell DESTINATION = Cell.at(3, 3);
        private static final Set<Character> OPEN_CHARS = Set.of('b', 'c', 'd', 'e', 'f');
        private static final List<Direction> DIRECTIONS =
                List.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);

        public List<Path> findPaths() {
            final List<Path> paths = new ArrayList<>();
            final Deque<Path> q = new ArrayDeque<>();
            q.add(new Path("", START));
            while (!q.isEmpty()) {
                final Path path = q.pollFirst();
                if (path.isAt(DESTINATION)) {
                    paths.add(path);
                    continue;
                }
                adjacent(path).forEach(q::addLast);
            }
            return paths;
        }

        private Stream<Path> adjacent(final Path path) {
            final Map<Direction, Boolean> openDoors = openDoors(path);
            return DIRECTIONS.stream()
                    .filter(openDoors::get)
                    .map(
                            direction ->
                                    new Path(
                                            path.path() + direction.getLetter().get(),
                                            path.position().at(direction)))
                    .filter(n -> isInBounds(n.position()));
        }

        private Map<Direction, Boolean> openDoors(final Path path) {
            final String data =
                    new StringBuilder().append(this.salt).append(path.path()).toString();
            final String md5Hex = MD5.md5Hex(data);
            return Range.range(DIRECTIONS.size()).stream()
                    .collect(toMap(DIRECTIONS::get, i -> OPEN_CHARS.contains(md5Hex.charAt(i))));
        }

        private boolean isInBounds(final Cell position) {
            return position.getCol() >= START.getCol()
                    && position.getRow() >= START.getRow()
                    && position.getCol() <= DESTINATION.getCol()
                    && position.getRow() <= DESTINATION.getRow();
        }
    }
}
