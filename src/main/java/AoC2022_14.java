import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.Range;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public class AoC2022_14 extends AoCBase {
    
    private static final Position START = Position.of(500, 0);

    private final Set<Position> rocks;
    private final int minX;
    private final int maxX;
    private final int maxY;
    
    private AoC2022_14(final List<String> input, final boolean debug) {
        super(debug);
        this.rocks = new HashSet<>();
        for (final String line : input) {
            final String[] splits = line.split(" -> ");
            Range.range(1, splits.length, 1).forEach(i -> {
                final String[] splits1 = splits[i - 1].split(",");
                final String[] splits2 = splits[i].split(",");
                final int x1 = Integer.parseInt(splits1[0]);
                final int y1 = Integer.parseInt(splits1[1]);
                final int x2 = Integer.parseInt(splits2[0]);
                final int y2 = Integer.parseInt(splits2[1]);
                if (x1 == x2) {
                    if (y1 < y2) {
                        Range.rangeClosed(y1, y2, 1).forEach(y ->
                            this.rocks.add(Position.of(x1, y)));
                    } else {
                        Range.rangeClosed(y2, y1, 1).forEach(y ->
                            this.rocks.add(Position.of(x1, y)));
                    }
                } else {
                    if (x1 < x2) {
                        Range.rangeClosed(x1, x2, 1).forEach(x ->
                            this.rocks.add(Position.of(x, y1)));
                    } else {
                        Range.rangeClosed(x2, x1, 1).forEach(x ->
                            this.rocks.add(Position.of(x, y1)));
                    }
                }
            });
        }
        final IntSummaryStatistics stats = this.rocks.stream()
                .mapToInt(Position::getX)
                .summaryStatistics();
        this.minX = stats.getMin();
        this.maxX = stats.getMax();
        this.maxY = this.rocks.stream().mapToInt(Position::getY).max().getAsInt();
        log(this.rocks.size());
        log(this.rocks);
        log(String.format("maxY: %d", maxY));
    }
    
    public static final AoC2022_14 create(final List<String> input) {
        return new AoC2022_14(input, false);
    }

    public static final AoC2022_14 createDebug(final List<String> input) {
        return new AoC2022_14(input, true);
    }
    
    Position drop(final Set<Position> sand, final int maxY) {
        Position curr = START;
        while (true) {
            final Position p = curr;
            final Position down = Position.of(curr.getX(), curr.getY() + 1);
            final Position downLeft = Position.of(curr.getX() - 1, curr.getY() + 1);
            final Position downRight = Position.of(curr.getX() + 1, curr.getY() + 1);
            for (final Position test : List.of(down, downLeft, downRight)) {
                if (!this.rocks.contains(test) && !sand.contains(test)) {
                    curr = test;
                    break;
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
        final Set<Position> sand = new HashSet<>();
        int ans = 0;
        while (true) {
            final Position p = drop(sand, maxY);
            log(p);
            if (p == null) {
                break;
            }
            sand.add(p);
            ans++;
            if (p.equals(START)) {
                break;
            }
        }
        return ans;
    }
    
    @Override
    public Integer solvePart1() {
        return solve(this.maxY);
    }

    @Override
    public Integer solvePart2() {
        Range.rangeClosed(minX - 100, maxX + 100, 1)
            .forEach(x -> this.rocks.add(Position.of(x, this.maxY + 2)));
        return solve(this.maxY + 2);
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

    private static final List<String> TEST = splitLines(
        "498,4 -> 498,6 -> 496,6\r\n" +
        "503,4 -> 502,4 -> 502,9 -> 494,9"
    );
}
