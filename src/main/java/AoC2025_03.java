import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2025_03 extends SolutionBase<List<String>, Long, Long> {

    private AoC2025_03(final boolean debug) {
        super(debug);
    }

    public static AoC2025_03 create() {
        return new AoC2025_03(false);
    }

    public static AoC2025_03 createDebug() {
        return new AoC2025_03(true);
    }

    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }

    private long solveLine(final String line, final int digits) {
        int pos = 0;
        final StringBuilder sb = new StringBuilder();
        for (int i = digits; i > 0; i--) {
            char best = '0';
            for (int j = pos; j < line.length() - i + 1; j++) {
                final char val = line.charAt(j);
                if (val > best) {
                    best = val;
                    pos = j + 1;
                }
            }
            sb.append(best);
        }
        return Long.parseLong(sb.toString());
    }

    private long solve(final List<String> lines, final int digits) {
        return lines.stream().mapToLong(line -> solveLine(line, digits)).sum();
    }

    @Override
    public Long solvePart1(final List<String> lines) {
        return this.solve(lines, 2);
    }

    @Override
    public Long solvePart2(final List<String> lines) {
        return this.solve(lines, 12);
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "357"),
        @Sample(method = "part2", input = TEST, expected = "3121910778619"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            987654321111111
            811111111111119
            234234234234278
            818181911112111
            """;
}
