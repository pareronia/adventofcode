import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_14 extends AoCBase {
    
    private static final Position SOURCE = Position.of(500, 0);
    private static final char START = '+';
    private static final char EMPTY = ' ';
    private static final char ROCK = '▒';
    private static final char SAND = 'o';

    private final Set<Position> rocks;
    private final int minX;
    private final int maxX;
    private final int maxY;
    private final boolean[][] occupied;
    
    private AoC2022_14(final List<String> input, final boolean debug) {
        super(debug);
        this.rocks = new HashSet<>();
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
                        this.rocks.add(Position.of(x,  y))));
            });
        }
        final var stats = this.rocks.stream().mapToInt(Position::getX)
                .summaryStatistics();
        this.minX = stats.getMin();
        this.maxX = stats.getMax();
        this.maxY = this.rocks.stream().mapToInt(Position::getY).max().getAsInt();
        this.occupied = new boolean[maxY + 2][maxX + 150];
        rocks.stream().forEach(p -> occupied[p.getY()][p.getX()] = true);
    }
    
    public static final AoC2022_14 create(final List<String> input) {
        return new AoC2022_14(input, false);
    }

    public static final AoC2022_14 createDebug(final List<String> input) {
        return new AoC2022_14(input, true);
    }
    
    Position drop(final int maxY) {
        Position curr = SOURCE;
        while (true) {
            final int y = curr.getY() + 1;
            final Position p = curr;
            for (final int x : List.of(0, -1 , 1)) {
                try {
                    if (!this.occupied[y][curr.getX() + x]) {
                        curr = Position.of(curr.getX() + x, y);
                        break;
                    }
                } catch (final ArrayIndexOutOfBoundsException e) {
                }
            }
            if (curr.equals(p)) {
                return curr;
            }
            if (curr.getY() > maxY) {
                return null;
            }
        }
    }
    
    private int solve(final int maxY) {
        int cnt = 0;
        while (true) {
            final Position p = drop(maxY);
            if (p == null) {
                break;
            }
            this.occupied[p.getY()][p.getX()] = true;
            cnt++;
            if (p.equals(SOURCE)) {
                break;
            }
        }
        return cnt;
    }
    
    void draw() {
        if (!this.debug) {
            return;
        }
        final var statsX = this.rocks.stream().mapToInt(Position::getX)
            .summaryStatistics();
        final var statsY = this.rocks.stream().mapToInt(Position::getY)
            .summaryStatistics();
        IntStream.rangeClosed(SOURCE.getY(), statsY.getMax()).forEach(y -> {
            final var line = IntStream.rangeClosed(statsX.getMin(), statsX.getMax())
                    .mapToObj(x -> {
                        final Position pos = Position.of(x, y);
                        if (this.rocks.contains(pos)) {
                            return ROCK;
                        } else if (this.occupied[pos.getY()][pos.getX()]) {
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
    public Integer solvePart1() {
        final int ans = solve(this.maxY);
        draw();
        return ans;
    }

    @Override
    public Integer solvePart2() {
        final int max = this.maxY + 2;
        IntStream.rangeClosed(minX - max, maxX + max)
            .forEach(x -> this.rocks.add(Position.of(x, max)));
        final int ans = solve(max);
        draw();
        return ans;
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_14.createDebug(TEST).solvePart1() == 24;
        assert AoC2022_14.createDebug(TEST).solvePart2() == 93;

        final Puzzle puzzle = Aocd.puzzle(2022, 14);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_14.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_14.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines("""
        498,4 -> 498,6 -> 496,6
        503,4 -> 502,4 -> 502,9 -> 494,9
        """);
}
