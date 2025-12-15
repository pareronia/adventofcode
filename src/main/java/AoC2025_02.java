import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2025_02 extends SolutionBase<List<AoC2025_02.Range>, Long, Long> {

    private AoC2025_02(final boolean debug) {
        super(debug);
    }

    public static AoC2025_02 create() {
        return new AoC2025_02(false);
    }

    public static AoC2025_02 createDebug() {
        return new AoC2025_02(true);
    }

    @Override
    protected List<Range> parseInput(final List<String> inputs) {
        return Arrays.stream(inputs.getFirst().split(","))
                .map(
                        s -> {
                            final StringSplit sp = StringOps.splitOnce(s, "-");
                            return new Range(Long.parseLong(sp.left()), Long.parseLong(sp.right()));
                        })
                .toList();
    }

    private long solve(final List<Range> ranges, final Mode mode) {
        return ranges.stream()
                .flatMapToLong(
                        range -> LongStream.rangeClosed(range.from, range.to).filter(mode::check))
                .sum();
    }

    @Override
    public Long solvePart1(final List<Range> ranges) {
        return this.solve(ranges, Mode.EXACTLY_ONCE);
    }

    @Override
    public Long solvePart2(final List<Range> ranges) {
        return this.solve(ranges, Mode.AT_LEAST_ONCE);
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "1227775554"),
        @Sample(method = "part2", input = TEST, expected = "4174379265"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,\
            446443-446449,38593856-38593862,565653-565659,824824821-824824827,\
            2121212118-2121212124
            """;

    record Range(long from, long to) {}

    enum Mode {
        EXACTLY_ONCE,
        AT_LEAST_ONCE;

        @SuppressWarnings("PMD.OnlyOneReturn")
        private boolean subCheck(final String str, final int sublen) {
            final int len = str.length();
            if (len % sublen != 0) {
                return false;
            }
            final String sub = str.substring(0, sublen);
            for (int i = sublen; i < len; i += sublen) {
                if (!str.startsWith(sub, i)) {
                    return false;
                }
            }
            return true;
        }

        boolean check(final long n) {
            final String str = String.valueOf(n);
            return switch (this) {
                case EXACTLY_ONCE -> str.length() % 2 == 0 && subCheck(str, str.length() / 2);
                case AT_LEAST_ONCE -> {
                    for (int i = 1; i <= str.length() / 2; i++) {
                        if (subCheck(str, i)) {
                            yield true;
                        }
                    }
                    yield false;
                }
            };
        }
    }
}
