import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2025_07 extends SolutionBase<List<String>, Long, Long> {

    public static final char SPLITTER = '^';
    public static final char START = 'S';

    private AoC2025_07(final boolean debug) {
        super(debug);
    }

    public static AoC2025_07 create() {
        return new AoC2025_07(false);
    }

    public static AoC2025_07 createDebug() {
        return new AoC2025_07(true);
    }

    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private Result solve(final List<String> grid) {
        final int start = grid.getFirst().indexOf(START);
        long[] beams = new long[grid.getFirst().length()];
        beams[start] = 1;
        long splits = 0;
        for (int r = 1; r < grid.size(); r++) {
            final char[] row = grid.get(r).toCharArray();
            final long[] newBeams = new long[row.length];
            for (int c = 0; c < row.length; c++) {
                if (row[c] == SPLITTER) {
                    newBeams[c - 1] += beams[c];
                    newBeams[c + 1] += beams[c];
                    splits += (beams[c] > 0 ? 1 : 0);
                } else {
                    newBeams[c] += beams[c];
                }
            }
            beams = newBeams;
        }
        return new Result(splits, Arrays.stream(beams).sum());
    }

    @Override
    public Long solvePart1(final List<String> grid) {
        return this.solve(grid).splits;
    }

    @Override
    public Long solvePart2(final List<String> grid) {
        return this.solve(grid).beams;
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "21"),
        @Sample(method = "part2", input = TEST, expected = "40"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            .......S.......
            ...............
            .......^.......
            ...............
            ......^.^......
            ...............
            .....^.^.^.....
            ...............
            ....^.^...^....
            ...............
            ...^.^...^.^...
            ...............
            ..^...^.....^..
            ...............
            .^.^.^.^.^...^.
            ...............
            """;

    private record Result(long splits, long beams) {}
}
