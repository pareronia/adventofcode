import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2022_14
                extends SolutionBase<AoC2022_14.Cave, Integer, Integer> {
    
    public static final Position SOURCE = Position.of(500, 0);
    private static final char START = '+';
    private static final char EMPTY = ' ';
    private static final char ROCK = '▒';
    private static final char SAND = 'o';
    
    private final List<Listener> listeners = new ArrayList<>();

    private AoC2022_14(final boolean debug) {
        super(debug);
    }
    
    public static final AoC2022_14 create() {
        return new AoC2022_14(false);
    }

    public static final AoC2022_14 createDebug() {
        return new AoC2022_14(true);
    }
    
    public void addListener(final Listener listener) {
        this.listeners.add(listener);
    }
    
    Optional<Position> drop(final boolean[][] occupied, final int maxY) {
        Position curr = SOURCE;
        while (true) {
            final int y = curr.getY() + 1;
            final Position p = curr;
            for (final int x : List.of(0, -1 , 1)) {
                try {
                    if (!occupied[y][curr.getX() + x]) {
                        curr = Position.of(curr.getX() + x, y);
                        break;
                    }
                } catch (final ArrayIndexOutOfBoundsException e) {
                }
            }
            if (curr.equals(p)) {
                return Optional.of(curr);
            }
            if (curr.getY() > maxY) {
                return Optional.empty();
            }
        }
    }
    
    private int solve(final Cave cave, final int maxY) {
        this.listeners.forEach(l -> l.start(cave));
        int cnt = 0;
        while (true) {
            final Optional<Position> p = drop(cave.occupied, maxY);
            if (p.isPresent()) {
                cave.occupied[p.get().getY()][p.get().getX()] = true;
                this.listeners.forEach(l -> l.stateUpdated(cave));
                cnt++;
                if (p.get().equals(SOURCE)) {
                    break;
                }
            } else {
                break;
            }
        }
        this.listeners.forEach(Listener::close);
        return cnt;
    }
    
    void draw(final Cave cave) {
        if (!this.debug) {
            return;
        }
        final var statsX = IntStream.range(0, cave.occupied.length)
            .flatMap(y -> IntStream.range(0, cave.occupied[y].length)
            .filter(x -> cave.occupied[y][x]))
            .summaryStatistics();
        IntStream.rangeClosed(SOURCE.getY(), cave.maxY).forEach(y -> {
            final var line = IntStream.rangeClosed(statsX.getMin(), statsX.getMax())
                    .mapToObj(x -> {
                        final Position pos = Position.of(x, y);
                        if (cave.rocks.contains(pos)) {
                            return ROCK;
                        } else if (cave.occupied[pos.getY()][pos.getX()]) {
                            return SAND;
                        } else if (SOURCE.equals(pos)) {
                            return START;
                        }
                        return EMPTY;
                    })
                    .collect(toAString());
            log(line);
        });
    }
    
    @Override
    protected Cave parseInput(final List<String> inputs) {
        return Cave.fromInput(inputs);
    }

    @Override
    public Integer solvePart1(final Cave cave) {
        final int ans = solve(cave, cave.maxY);
        draw(cave);
        return ans;
    }

    @Override
    public Integer solvePart2(final Cave cave) {
        final Cave newCave = Cave.withRocks(cave.rocks);
        final int ans = solve(newCave, cave.maxY + 2);
        draw(newCave);
        return ans;
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "24"),
        @Sample(method = "part2", input = TEST, expected = "93"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2022_14.create().run();
    }

    protected static final String TEST = """
        498,4 -> 498,6 -> 496,6
        503,4 -> 502,4 -> 502,9 -> 494,9
        """;

    record Cave(Set<Position> rocks, boolean[][] occupied, int maxY) {
        
        public static Cave withRocks(final Set<Position> rocks) {
            final int maxX = rocks.stream().mapToInt(Position::getX).max().getAsInt();
            final int maxY = rocks.stream().mapToInt(Position::getY).max().getAsInt();
            final var occupied = new boolean[maxY + 2][maxX + 150];
            rocks.stream().forEach(p -> occupied[p.getY()][p.getX()] = true);
            return new Cave(rocks, occupied, maxY);
        }

        public static Cave fromInput(final List<String> input) {
            final Set<Position> rocks = new HashSet<>();
            for (final String line : input) {
                final var splits = line.split(" -> ");
                IntStream.range(1, splits.length).forEach(i -> {
                    final var splits1 = splits[i - 1].split(",");
                    final var splits2 = splits[i].split(",");
                    final var xs = List.of(splits1, splits2).stream()
                            .map(a -> a[0])
                            .map(Integer::parseInt)
                            .sorted()
                            .collect(toList());
                    final var ys = List.of(splits1, splits2).stream()
                            .map(a -> a[1])
                            .map(Integer::parseInt)
                            .sorted()
                            .collect(toList());
                    IntStream.rangeClosed(xs.get(0), xs.get(1)).forEach(x ->
                        IntStream.rangeClosed(ys.get(0), ys.get(1)).forEach(y ->
                            rocks.add(Position.of(x,  y))));
                });
            }
            return Cave.withRocks(rocks);
        }
    }
    
    public interface Listener {
        void start(Cave cave);
        
        void stateUpdated(Cave cave);
        
        default void close() {}
    }
}
