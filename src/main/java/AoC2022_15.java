import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Range;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.geometry.Position;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.EqualsAndHashCode;
import lombok.ToString;

public class AoC2022_15 extends AoCBase {
    
    private final Set<Sensor> sensors;
    private final Map<Integer, Set<Integer>> beacons;
    
    private AoC2022_15(final List<String> input, final boolean debug) {
        super(debug);
        this.sensors = new HashSet<>();
        this.beacons = new HashMap<>();
        for (final String line : input) {
            final int[] nums = Utils.integerNumbers(line);
            final Sensor sensor = new Sensor(nums[0], nums[1], nums[2], nums[3]);
            this.sensors.add(sensor);
            this.beacons.computeIfAbsent(nums[3], k -> new HashSet<>()).add(nums[2]);
        }
        log(this.sensors);
        log(this.beacons);
    }
    
    public static final AoC2022_15 create(final List<String> input) {
        return new AoC2022_15(input, false);
    }

    public static final AoC2022_15 createDebug(final List<String> input) {
        return new AoC2022_15(input, true);
    }
    
    private Deque<Range<Integer>> getRanges(final int y) {
        final Set<Range<Integer>> ranges = new HashSet<>();
        for (final Sensor sensor : this.sensors) {
            if (Math.abs(sensor.y - y) > sensor.distanceToBeacon) {
                continue;
            }
            ranges.add(sensor.xRangeAt(y));
        }
        return RangeMerger.mergeRanges(ranges);
    }
    
    private int solve1(final int y) {
        final Set<Integer> beaconsXs = this.beacons.get(y);
        return (int) getRanges(y).stream()
            .mapToLong(r -> r.getMaximum() - r.getMinimum() + 1
                    - beaconsXs.stream().filter(r::contains).count())
            .sum();
    }
    
    private long solve2(final int max) {
        for (int y = 0; y <= max; y++) {
            final Deque<Range<Integer>> ranges = getRanges(y);
            for (int x = 0; x <= max; x++) {
                for (final Range<Integer> merged : ranges) {
                    if (merged.isAfter(x)) {
                        log(Position.of(x, y));
                        return x * 4_000_000L + y;
                    }
                    x = merged.getMaximum() + 1;
                }
            }
        }
        throw new IllegalStateException("Unsolvable");
    }
    
    @Override
    public Integer solvePart1() {
        return solve1(2_000_000);
    }

    @Override
    public Long solvePart2() {
        return solve2(4_000_000);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2022_15.createDebug(TEST).solve1(10) == 26;
        assert AoC2022_15.createDebug(TEST).solve2(20) == 56_000_011L;

        final Puzzle puzzle = Aocd.puzzle(2022, 15);
        final List<String> inputData = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", AoC2022_15.create(inputData)::solvePart1),
            () -> lap("Part 2", AoC2022_15.create(inputData)::solvePart2)
        );
    }

    private static final List<String> TEST = splitLines(
        "Sensor at x=2, y=18: closest beacon is at x=-2, y=15\r\n" +
        "Sensor at x=9, y=16: closest beacon is at x=10, y=16\r\n" +
        "Sensor at x=13, y=2: closest beacon is at x=15, y=3\r\n" +
        "Sensor at x=12, y=14: closest beacon is at x=10, y=16\r\n" +
        "Sensor at x=10, y=20: closest beacon is at x=10, y=16\r\n" +
        "Sensor at x=14, y=17: closest beacon is at x=10, y=16\r\n" +
        "Sensor at x=8, y=7: closest beacon is at x=2, y=10\r\n" +
        "Sensor at x=2, y=0: closest beacon is at x=2, y=10\r\n" +
        "Sensor at x=0, y=11: closest beacon is at x=2, y=10\r\n" +
        "Sensor at x=20, y=14: closest beacon is at x=25, y=17\r\n" +
        "Sensor at x=17, y=20: closest beacon is at x=21, y=22\r\n" +
        "Sensor at x=16, y=7: closest beacon is at x=15, y=3\r\n" +
        "Sensor at x=14, y=3: closest beacon is at x=15, y=3\r\n" +
        "Sensor at x=20, y=1: closest beacon is at x=15, y=3"
    );
    
    @EqualsAndHashCode
    @ToString
    private static final class Sensor {
        private final int x;
        private final int y;
        private final int distanceToBeacon;
        
        public Sensor(final int x, final int y, final int beaconX, final int beaconY) {
            this.x = x;
            this.y = y;
            this.distanceToBeacon = Math.abs(beaconX - x) + Math.abs(beaconY - y);
        }
        
        public Range<Integer> xRangeAt(final int y) {
            final int dy = Math.abs(this.y - y);
            assert dy <= distanceToBeacon;
            return Range.between(
                    x - distanceToBeacon + dy,
                    x + distanceToBeacon - dy);
        }
    }
    
    static final class RangeMerger {

        public static Deque<Range<Integer>> mergeRanges(final Set<Range<Integer>> ranges) {
            final Deque<Range<Integer>> m = new ArrayDeque<>();
            final List<Range<Integer>> sorted = new ArrayList<>(ranges);
            Collections.sort(sorted, (r1, r2) -> {
                final int first = Integer.compare(r1.getMinimum(), r2.getMinimum());
                if (first == 0) {
                    return Integer.compare(r1.getMaximum(), r2.getMaximum());
                }
                return first;
            });
            for (final Range<Integer> range : sorted) {
                if (m.isEmpty()) {
                    m.addLast(range);
                    continue;
                }
                final Range<Integer> last = m.peekLast();
                if (range.isOverlappedBy(last)) {
                    m.removeLast();
                    m.add(Range.between(
                        last.getMinimum(),
                        Math.max(last.getMaximum(), range.getMaximum())));
                } else {
                    m.addLast(range);
                }
            }
            return m;
        }
    }
}
