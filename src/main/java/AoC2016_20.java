import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import com.github.pareronia.aoc.RangeInclusive;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"PMD.NoPackage", "PMD.ClassNamingConventions"})
public final class AoC2016_20 extends SolutionBase<List<RangeInclusive<Long>>, Long, Long> {

    private static final long TOTAL_IPS = 4_294_967_296L;

    private AoC2016_20(final boolean debug) {
        super(debug);
    }

    public static AoC2016_20 create() {
        return new AoC2016_20(false);
    }

    public static AoC2016_20 createDebug() {
        return new AoC2016_20(true);
    }

    @Override
    protected List<RangeInclusive<Long>> parseInput(final List<String> inputs) {
        final List<RangeInclusive<Long>> ranges =
                inputs.stream()
                        .map(s -> s.split("-"))
                        .map(sp -> RangeInclusive.between(Long.valueOf(sp[0]), Long.valueOf(sp[1])))
                        .collect(toList());
        return combineRanges(ranges);
    }

    // TODO: unduplicate w/ RangeInclusive.mergeRanges
    private List<RangeInclusive<Long>> combineRanges(final List<RangeInclusive<Long>> ranges) {
        final List<RangeInclusive<Long>> combines = new ArrayList<>();
        // why doesn't this work without sorting?
        ranges.sort(comparing(RangeInclusive::getMinimum));
        combines.add(ranges.get(0));
        for (int i = 1; i < ranges.size(); i++) {
            final RangeInclusive<Long> r = ranges.get(i);
            boolean combined = false;
            for (int j = 0; j < combines.size(); j++) {
                final RangeInclusive<Long> combine = combines.get(j);
                if (combine.isOverlappedBy(r)) {
                    final long min = Math.min(combine.getMinimum(), r.getMinimum());
                    final long max = Math.max(combine.getMaximum(), r.getMaximum());
                    combines.set(j, RangeInclusive.between(min, max));
                    combined = true;
                } else if (r.getMaximum() + 1 == combine.getMinimum()) {
                    combines.set(j, RangeInclusive.between(r.getMinimum(), combine.getMaximum()));
                    combined = true;
                } else if (combine.getMaximum() + 1 == r.getMinimum()) {
                    combines.set(j, RangeInclusive.between(combine.getMinimum(), r.getMaximum()));
                    combined = true;
                }
            }
            if (!combined) {
                combines.add(r);
            }
        }
        return Collections.unmodifiableList(combines);
    }

    @Override
    public Long solvePart1(final List<RangeInclusive<Long>> ranges) {
        return ranges.stream().findFirst().map(c -> c.getMaximum() + 1).orElseThrow();
    }

    @Override
    public Long solvePart2(final List<RangeInclusive<Long>> ranges) {
        return ranges.stream()
                .map(c -> c.getMaximum() - c.getMinimum() + 1)
                .reduce(TOTAL_IPS, (a, b) -> a - b);
    }

    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "3"),
        @Sample(method = "part1", input = TEST2, expected = "90000101"),
        @Sample(method = "part2", input = TEST1, expected = "4294967288"),
        @Sample(method = "part2", input = TEST2, expected = "4194967190"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST1 =
            """
            5-8
            0-2
            4-7
            """;
    private static final String TEST2 =
            """
            0-90000000
            100000005-110000005
            90000001-90000100
            1000000054-1000000057
            """;
}
