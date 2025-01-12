import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.pareronia.aoc.IterTools;
import com.github.pareronia.aoc.RangeInclusive;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2022_15 extends SolutionBase<AoC2022_15.Input, Integer, Long> {
    
    private AoC2022_15(final boolean debug) {
        super(debug);
    }
    
    public static final AoC2022_15 create() {
        return new AoC2022_15(false);
    }

    public static final AoC2022_15 createDebug() {
        return new AoC2022_15(true);
    }
    
    @Override
    protected Input parseInput(final List<String> inputs) {
        final Map<Position, Integer> sensors = new HashMap<>();
        final Set<Position> beacons = new HashSet<>();
        for (final String line : inputs) {
            final int[] nums = Utils.integerNumbers(line);
            final Position s = Position.of(nums[0], nums[1]);
            final Position b = Position.of(nums[2], nums[3]);
            sensors.put(s, s.manhattanDistance(b));
            beacons.add(b);
        }
        return new Input(sensors, beacons);
    }

    private int solve1(final Input input, final int y) {
        final var ranges = new ArrayList<RangeInclusive<Integer>>();
        for (final Entry<Position, Integer> entry : input.sensors.entrySet()) {
            final Position s = entry.getKey();
            final int md = entry.getValue();
            final int dy = Math.abs(s.getY() - y);
            if (dy > md) {
                continue;
            }
            ranges.add(RangeInclusive.between(
                                s.getX() - md + dy, s.getX() + md - dy));
        }
        return (int) RangeInclusive.mergeRanges(ranges).stream()
            .mapToLong(r ->
                r.getMaximum() - r.getMinimum() + 1
                - input.beacons.stream()
                    .filter(b -> b.getY() == y && r.contains(b.getX()))
                    .count())
            .sum();
    }
    
    private long solve2(final Map<Position, Integer> sensors, final int max) {
        final Set<Integer> acoeffs = new HashSet<>();
        final Set<Integer> bcoeffs = new HashSet<>();
        for (final Entry<Position, Integer> entry : sensors.entrySet()) {
            final Position s = entry.getKey();
            final int md = entry.getValue();
            acoeffs.add(s.getY() - s.getX() + md + 1);
            acoeffs.add(s.getY() - s.getX() - md - 1);
            bcoeffs.add(s.getX() + s.getY() + md + 1);
            bcoeffs.add(s.getX() + s.getY() - md - 1);
        }
        return IterTools.product(acoeffs, bcoeffs).stream()
            .filter(ab -> ab.first() < ab.second())
            .filter(ab -> (ab.second() - ab.first()) % 2 == 0)
            .map(ab -> Position.of(
                    (ab.second() - ab.first()) / 2,
                    (ab.first() + ab.second()) / 2))
            .filter(p -> 0 < p.getX() && p.getX() < max)
            .filter(p -> 0 < p.getY() && p.getY() < max)
            .filter(p -> sensors.keySet().stream().allMatch(
                            s -> p.manhattanDistance(s) > sensors.get(s)))
            .mapToLong(p -> 4_000_000L * p.getX() + p.getY())
            .findFirst().orElseThrow();
    }
    
    @Override
    public Integer solvePart1(final Input input) {
        return solve1(input, 2_000_000);
    }

    @Override
    public Long solvePart2(final Input input) {
        return solve2(input.sensors, 4_000_000);
    }
    
    record Input(Map<Position, Integer> sensors, Set<Position> beacons) {}
    
    @Override
    public void samples() {
        final AoC2022_15 test = AoC2022_15.createDebug();
        final Input input = test.parseInput(StringOps.splitLines(TEST));
        assert test.solve1(input, 10) == 26;
        assert test.solve2(input.sensors, 20) == 56_000_011L;
    }

    public static void main(final String[] args) throws Exception {
        AoC2022_15.create().run();
    }

    private static final String TEST = """
        Sensor at x=2, y=18: closest beacon is at x=-2, y=15
        Sensor at x=9, y=16: closest beacon is at x=10, y=16
        Sensor at x=13, y=2: closest beacon is at x=15, y=3
        Sensor at x=12, y=14: closest beacon is at x=10, y=16
        Sensor at x=10, y=20: closest beacon is at x=10, y=16
        Sensor at x=14, y=17: closest beacon is at x=10, y=16
        Sensor at x=8, y=7: closest beacon is at x=2, y=10
        Sensor at x=2, y=0: closest beacon is at x=2, y=10
        Sensor at x=0, y=11: closest beacon is at x=2, y=10
        Sensor at x=20, y=14: closest beacon is at x=25, y=17
        Sensor at x=17, y=20: closest beacon is at x=21, y=22
        Sensor at x=16, y=7: closest beacon is at x=15, y=3
        Sensor at x=14, y=3: closest beacon is at x=15, y=3
        Sensor at x=20, y=1: closest beacon is at x=15, y=3
        """;
}
