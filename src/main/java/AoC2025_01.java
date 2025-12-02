import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2025_01 extends SolutionBase<List<Long>, Long, Long> {

    private static final long START = 50;
    private static final long TOTAL = 100;

    private AoC2025_01(final boolean debug) {
        super(debug);
    }

    public static AoC2025_01 create() {
        return new AoC2025_01(false);
    }

    public static AoC2025_01 createDebug() {
        return new AoC2025_01(true);
    }

    @Override
    protected List<Long> parseInput(final List<String> inputs) {
        return inputs.stream()
                .map(s -> (s.charAt(0) == 'R' ? 1 : -1) * Long.parseLong(s.substring(1)))
                .toList();
    }

    @Override
    public Long solvePart1(final List<Long> rotations) {
        long ans = 0L;
        long dial = START;
        for (final long rot : rotations) {
            dial = (dial + rot) % TOTAL;
            if (dial == 0) {
                ans++;
            }
        }
        return ans;
    }

    @Override
    public Long solvePart2(final List<Long> rotations) {
        long ans = 0L;
        long dial = START;
        for (final long rot : rotations) {
            final long div = rot / (rot < 0 ? -TOTAL : TOTAL);
            final long mod = rot % (rot < 0 ? -TOTAL : TOTAL);
            ans += div;
            if ((rot < 0 && dial != 0 && dial + mod <= 0) || (rot > 0 && dial + mod >= TOTAL)) {
                ans++;
            }
            dial = Math.floorMod(dial + rot, TOTAL);
        }
        return ans;
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "3"),
        @Sample(method = "part2", input = TEST, expected = "6"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            L68
            L30
            R48
            L5
            R60
            L55
            L1
            L99
            R14
            L82
            """;

    record Lists(List<Integer> left, List<Integer> right) {}
}
