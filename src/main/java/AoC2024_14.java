import static com.github.pareronia.aoc.StringOps.splitLines;
import static com.github.pareronia.aoc.StringOps.splitOnce;

import java.util.Arrays;
import java.util.List;

import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.geometry.Vector;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_14
        extends SolutionBase<List<AoC2024_14.Robot>, Integer, Integer> {
    
    private static final int W = 101;
    private static final int H = 103;
    
    private AoC2024_14(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_14 create() {
        return new AoC2024_14(false);
    }
    
    public static AoC2024_14 createDebug() {
        return new AoC2024_14(true);
    }
    
    @Override
    protected List<Robot> parseInput(final List<String> inputs) {
        return inputs.stream().map(Robot::fromInput).toList();
    }
    
    private int move(
            final List<Robot> robots,
            final int w,
            final int h,
            final int rounds
    ) {
        final List<Position> moved = robots.stream()
            .map(r -> r.position.translate(r.velocity, rounds))
            .toList();
        final int[] q = {0, 0, 0, 0};
        moved.forEach(p -> {
            final int px = Math.floorMod(p.getX(), w);
            final int py = Math.floorMod(p.getY(), h);
            if (0 <= px && px < w / 2) {
                if (0 <= py && py < h / 2) {
                    q[0] += 1;
                } else if (h / 2 + 1 <= py && py < h) {
                    q[1] += 1;
                }
            } else if (w / 2 + 1 <= px && px < w) {
                if (0 <= py && py < h / 2) {
                    q[2] += 1;
                } else if (h / 2 + 1 <= py && py < h) {
                    q[3] += 1;
                }
            }
        });
        return Arrays.stream(q).reduce(1, (a, b) -> a * b);
    }
    
    private int solve1(final List<Robot> robots, final int w, final int h) {
        return move(robots, w, h, 100);
    }
    
    @Override
    public Integer solvePart1(final List<Robot> robots) {
        return solve1(robots, W, H);
    }
    
    @Override
    public Integer solvePart2(final List<Robot> robots) {
        int ans = 0;
        int best = Integer.MAX_VALUE;
        for (int round = 1; round <= W * H; round++) {
            final int safetyFactor = move(robots, W, H, round);
            if (safetyFactor < best) {
                best = safetyFactor;
                ans = round;
            }
        }
        return ans;
    }
    
    @Override
    public void samples() {
        final AoC2024_14 test = AoC2024_14.createDebug();
        final List<Robot> input = test.parseInput(splitLines(TEST));
        assert test.solve1(input, 11, 7) == 12;
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_14.create().run();
    }

    private static final String TEST = """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
            """;

    record Robot(Position position, Vector velocity) {

        public static Robot fromInput(final String line) {
            final StringSplit split = splitOnce(line, " ");
            final StringSplit p = splitOnce(
                    splitOnce(split.left(), "=").right(),
                    ",");
            final StringSplit v = splitOnce(
                    splitOnce(split.right(), "=").right(),
                    ",");
            return new Robot(
                Position.of(
                        Integer.parseInt(p.left()),
                        Integer.parseInt(p.right())),
                Vector.of(
                        Integer.parseInt(v.left()),
                        Integer.parseInt(v.right()))
            );
        }
    }
}