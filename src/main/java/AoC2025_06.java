import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2025_06 extends SolutionBase<AoC2025_06.WorkSheet, Long, Long> {

    private AoC2025_06(final boolean debug) {
        super(debug);
    }

    public static AoC2025_06 create() {
        return new AoC2025_06(false);
    }

    public static AoC2025_06 createDebug() {
        return new AoC2025_06(true);
    }

    @Override
    protected WorkSheet parseInput(final List<String> inputs) {
        return WorkSheet.fromInput(inputs);
    }

    @SuppressWarnings("PMD.TooFewBranchesForSwitch")
    private long solve(final WorkSheet worksheet, final Mode mode) {
        return worksheet.getProblems(mode).stream()
                .mapToLong(
                        p ->
                                switch (p.operation) {
                                    case ADD -> Arrays.stream(p.nums).sum();
                                    case MULTIPLY ->
                                            Arrays.stream(p.nums).reduce(1L, (a, b) -> a * b);
                                })
                .sum();
    }

    @Override
    public Long solvePart1(final WorkSheet worksheet) {
        return this.solve(worksheet, Mode.BY_ROWS);
    }

    @Override
    public Long solvePart2(final WorkSheet worksheet) {
        return this.solve(worksheet, Mode.BY_COLUMNS);
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "4277556"),
        @Sample(method = "part2", input = TEST, expected = "3263827"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            123 328  51 64\s
             45 64  387 23\s
              6 98  215 314
            *   +   *   +\s\s
            """;

    record Problem(long[] nums, Operation operation) {

        enum Operation {
            ADD,
            MULTIPLY;

            public static Operation fromString(final String string) {
                return switch (string) {
                    case "+" -> ADD;
                    case "*" -> MULTIPLY;
                    default -> throw new IllegalArgumentException();
                };
            }
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(100);
            builder.append("Problem [nums=")
                    .append(Arrays.toString(nums))
                    .append(", operation=")
                    .append(operation)
                    .append(']');
            return builder.toString();
        }
    }

    enum Mode {
        BY_ROWS,
        BY_COLUMNS
    }

    record WorkSheet(List<String> strings) {

        public static WorkSheet fromInput(final List<String> inputs) {
            return new WorkSheet(inputs);
        }

        public Set<Problem> getProblems(final Mode mode) {
            final Set<Problem> problems = new HashSet<>();
            final CharGrid grid =
                    CharGrid.from(
                            range(this.strings.size() - 1).stream()
                                    .map(i -> this.strings.get(i) + " ")
                                    .toList());
            final List<Problem.Operation> ops =
                    Arrays.stream(this.strings.getLast().strip().split("\\s+"))
                            .map(Problem.Operation::fromString)
                            .toList();
            final List<String> nums = new ArrayList<>();
            int j = 0;
            for (int col = 0; col < grid.getWidth(); col++) {
                final String s = grid.getColumnAsString(col);
                if (s.isBlank()) {
                    final Stream<String> rows =
                            switch (mode) {
                                case BY_ROWS -> CharGrid.from(nums).getColumnsAsStrings();
                                case BY_COLUMNS -> CharGrid.from(nums).getRowsAsStrings();
                            };
                    problems.add(
                            new Problem(
                                    rows.mapToLong(row -> Long.parseLong(row.strip())).toArray(),
                                    ops.get(j)));
                    nums.clear();
                    j++;
                } else {
                    nums.add(s);
                }
            }
            return problems;
        }
    }
}
