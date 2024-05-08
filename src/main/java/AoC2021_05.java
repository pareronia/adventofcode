import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.Counter;
import com.github.pareronia.aoc.IntegerSequence.Range;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

/**
 * TODO Extract some geometry lib stuff from this
 */
public class AoC2021_05 extends SolutionBase<List<AoC2021_05.LineSegment>, Long, Long> {
    
    private AoC2021_05(final boolean debug) {
        super(debug);
    }

    public static final AoC2021_05 create() {
        return new AoC2021_05(false);
    }

    public static final AoC2021_05 createDebug() {
        return new AoC2021_05(true);
    }
    
    @Override
    protected List<LineSegment> parseInput(final List<String> inputs) {
        return inputs.stream().map(LineSegment::fromInput).toList();
    }

    private long countIntersections(
            final List<LineSegment> lines,
            final boolean diag
            ) {
        final Counter<Position> counter = new Counter<>(lines.stream()
                .filter(line -> diag || line.slopeX() == 0 || line.slopeY() == 0)
                .flatMap(LineSegment::positions));
        return counter.values().stream().filter(v -> v > 1).count();
    }
    
    @Override
    public Long solvePart1(final List<LineSegment> lines) {
        return countIntersections(lines, false);
    }

    @Override
    public Long solvePart2(final List<LineSegment> lines) {
        return countIntersections(lines, true);
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "5"),
        @Sample(method = "part2", input = TEST, expected = "12"),
    })
    public static void main(final String[] args) throws Exception {
        AoC2021_05.create().run();
    }

    private static final String TEST = """
            0,9 -> 5,9
            8,0 -> 0,8
            9,4 -> 3,4
            2,2 -> 2,1
            7,0 -> 7,4
            6,4 -> 2,0
            0,9 -> 2,9
            3,4 -> 1,4
            0,0 -> 8,8
            5,5 -> 8,2
            """;
    
    record LineSegment(int x1, int y1, int x2, int y2, int slopeX, int slopeY) {

        public static LineSegment fromInput(final String input) {
            final int[] p = Arrays.stream(input.split(" -> "))
                    .flatMap(q -> Arrays.stream(q.split(",")))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            return new LineSegment(
                    p[0], p[1], p[2], p[3],
                    p[0] == p[2] ? 0 : (p[0] < p[2] ? 1 : -1),
                    p[1] == p[3] ? 0 : (p[1] < p[3] ? 1 : -1));
        }

        public Stream<Position> positions() {
            final int len = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
            return Range.rangeClosed(len).intStream()
                .mapToObj(i -> Position.of(x1 + slopeX * i, y1 + slopeY * i));
        }
    }
}
