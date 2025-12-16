import com.github.pareronia.aoc.RangeInclusive;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2025_05 extends SolutionBase<AoC2025_05.Database, Long, Long> {

    private AoC2025_05(final boolean debug) {
        super(debug);
    }

    public static AoC2025_05 create() {
        return new AoC2025_05(false);
    }

    public static AoC2025_05 createDebug() {
        return new AoC2025_05(true);
    }

    @Override
    protected Database parseInput(final List<String> inputs) {
        return Database.fromInput(inputs);
    }

    @Override
    public Long solvePart1(final Database database) {
        return database.availableIds().stream()
                .filter(pid -> database.idRanges().stream().anyMatch(rng -> rng.contains(pid)))
                .count();
    }

    @Override
    public Long solvePart2(final Database database) {
        return database.idRanges().stream()
                .mapToLong(rng -> rng.getMaximum() - rng.getMinimum() + 1)
                .sum();
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "3"),
        @Sample(method = "part2", input = TEST, expected = "14"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            3-5
            10-14
            16-20
            12-18

            1
            5
            8
            11
            17
            32
            """;

    record Database(List<RangeInclusive<Long>> idRanges, List<Long> availableIds) {

        public static Database fromInput(final List<String> inputs) {
            final List<List<String>> blocks = StringOps.toBlocks(inputs);
            final Set<RangeInclusive<Long>> idRanges = new HashSet<>();
            for (final String line : blocks.getFirst()) {
                final StringSplit split = StringOps.splitOnce(line, "-");
                idRanges.add(
                        RangeInclusive.between(
                                Long.parseLong(split.left()), Long.parseLong(split.right())));
            }
            final List<Long> availableIds = blocks.getLast().stream().map(Long::parseLong).toList();
            return new Database(RangeInclusive.mergeRanges(idRanges), availableIds);
        }
    }
}
