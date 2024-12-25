import static com.github.pareronia.aoc.StringOps.splitLines;
import static com.github.pareronia.aoc.StringOps.splitOnce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.IntegerSequence.Range;
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
    
    private int getSafetyFactor(
            final List<Robot> robots,
            final int w,
            final int h,
            final int rounds
    ) {
        final int midW = w / 2;
        final int midH = h / 2;
        final int[] q = {0, 0, 0, 0};
        robots.stream()
            .map(r -> r.position.translate(r.velocity, rounds))
            .forEach(p -> {
                final int px = Math.floorMod(p.getX(), w);
                final int py = Math.floorMod(p.getY(), h);
                if (px < midW) {
                    if (py < midH) {
                        q[0] += 1;
                    } else if (midH < py) {
                        q[1] += 1;
                    }
                } else if (midW < px) {
                    if (py < midH) {
                        q[2] += 1;
                    } else if (midH < py) {
                        q[3] += 1;
                    }
                }
        });
        return Arrays.stream(q).reduce(1, (a, b) -> a * b);
    }
    
    private int solve1(final List<Robot> robots, final int w, final int h) {
        return getSafetyFactor(robots, w, h, 100);
    }
    
    @Override
    public Integer solvePart1(final List<Robot> robots) {
        return solve1(robots, W, H);
    }
    
    @Override
    public Integer solvePart2(final List<Robot> robots) {
        int ans = 0;
        int best = Integer.MAX_VALUE;
        int round = 1;
        final List<Integer> sfs = new ArrayList<>();
        while (round <= W + H) {
            final int safetyFactor = getSafetyFactor(robots, W, H, round);
            sfs.add(safetyFactor);
            if (safetyFactor < best) {
                best = safetyFactor;
                ans = round;
            }
            round++;
        }
        final List<Integer> mins = new ArrayList<>();
        Range.between(2, sfs.size() - 3).forEach(i -> {
            final int avg = Set.of(-2, -1, 1, 2).stream()
                    .mapToInt(j -> sfs.get(i + j)).sum() / 4;
            if (sfs.get(i) < avg * 9 / 10) {
                mins.add(i + 1);
            }
        });
        final int period = mins.get(2) - mins.get(0);
        round = mins.get(2) + period;
        while (round <= W * H) {
            final int safetyFactor = getSafetyFactor(robots, W, H, round);
            if (safetyFactor < best) {
                best = safetyFactor;
                ans = round;
            }
            round += period;
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