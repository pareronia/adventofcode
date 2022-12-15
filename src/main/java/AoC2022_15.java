import static com.github.pareronia.aoc.Utils.last;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
    private final Set<Position> beacons;
//    private final int minX;
//    private final int maxX;
    
    private AoC2022_15(final List<String> input, final boolean debug) {
        super(debug);
        this.sensors = new HashSet<>();
        this.beacons = new HashSet<>();
//        int minX = Integer.MAX_VALUE;
//        int maxX = Integer.MIN_VALUE;
        for (final String line : input) {
            final int[] nums = Utils.integerNumbers(line);
            final Sensor sensor = new Sensor(nums[0], nums[1], nums[2], nums[3]);
            this.sensors.add(sensor);
//            minX = Math.min(minX, sensor.minX());
//            maxX = Math.max(maxX, sensor.maxX());
            this.beacons.add(Position.of(nums[2], nums[3]));
        }
//        this.minX = minX;
//        this.maxX = maxX;
        log(this.sensors);
        log(this.beacons);
    }
    
    public static final AoC2022_15 create(final List<String> input) {
        return new AoC2022_15(input, false);
    }

    public static final AoC2022_15 createDebug(final List<String> input) {
        return new AoC2022_15(input, true);
    }
    
//    private void draw(final Map<Integer, Set<Range<Integer>>> ranges) {
//        if (!this.debug) {
//            return;
//        }
//        final IntSummaryStatistics statsY
//                = ranges.keySet().stream()
//                    .mapToInt(Integer::valueOf).summaryStatistics();
//        for (int y = statsY.getMin(); y <= statsY.getMax(); y++) {
//            final Set<Integer> mins = new HashSet<>();
//            final Set<Integer> maxs = new HashSet<>();
//            ranges.get(y).forEach(r -> {
//                mins.add(r.getMinimum());
//                maxs.add(r.getMaximum());
//            });
//            final StringBuilder line = new StringBuilder();
//            line.append(String.format("%03d : ", y));
//            for (int x = this.minX; x <= this.maxX; x++) {
//                final int thex = x;
//                final int they = y;
//                if (this.sensors.stream().anyMatch(s -> s.x == thex && s.y == they)) {
//                    line.append('S');
//                } else if (this.beacons.stream().anyMatch(b -> b.getX() == thex && b.getY() == they)) {
//                    line.append('B');
//                } else if (mins.contains(x) && maxs.contains(x)) {
//                    line.append('*');
//                } else if (mins.contains(x)) {
//                    line.append('[');
//                } else if (maxs.contains(x)) {
//                    line.append(']');
//                } else {
//                    line.append('.');
//                }
//            }
//            log(line);
//        }
//    }
//
//
    private List<Range<Integer>> getRanges(final int y) {
        final Set<Range<Integer>> ranges = new HashSet<>();
        for (final Sensor sensor : this.sensors) {
            if (y < sensor.minY() || y > sensor.maxY()) {
                continue;
            }
            ranges.add(sensor.xRangeAtAbs(y));
        }
        return RangeMerger.mergeRanges(ranges);
    }
    
    private int solve1(final int y) {
        int ans = 0;
        for (final Range<Integer> merged : getRanges(y)) {
            log(merged);
            for (int x = merged.getMinimum(); x <= merged.getMaximum(); x++) {
                if (this.beacons.contains(Position.of(x, y))) {
                    continue;
                }
                ans++;
            }
        }
        log(ans);
        return ans;
    }
    
    private long solve2(final int max) {
        for (int y = 0; y <= max; y++) {
            final List<Range<Integer>> ranges = getRanges(y);
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
            () -> lap("Part 1", AoC2022_15.createDebug(inputData)::solvePart1),
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
        
        public Range<Integer> xRangeAtAbs(final int y) {
            final int dy = Math.abs(this.y - y);
            assert dy <= distanceToBeacon;
            return xRangeAt(dy);
        }

        public Range<Integer> xRangeAt(final int dy) {
            return Range.between(
                    x - distanceToBeacon + Math.abs(dy),
                    x + distanceToBeacon - Math.abs(dy));
        }
        
        public int minY() {
            return y - distanceToBeacon;
        }
        
        public int maxY() {
            return y + distanceToBeacon;
        }
        
        public int minX() {
            return x - distanceToBeacon;
        }
        
        public int maxX() {
            return x + distanceToBeacon;
        }
    }
    
    static final class RangeMerger {

        public static List<Range<Integer>> mergeRanges(final Set<Range<Integer>> ranges) {
            final List<Range<Integer>> m = new ArrayList<>();
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
                    m.add(range);
                    continue;
                }
                final Range<Integer> last = last(m);
                if (range.isOverlappedBy(last)) {
                    m.remove(last);
                    m.add(Range.between(
                        last.getMinimum(),
                        Math.max(last.getMaximum(), range.getMaximum())));
                } else {
                    m.add(range);
                }
            }
            return m;
        }
    }
}
