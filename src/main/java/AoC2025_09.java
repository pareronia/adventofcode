import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.IterTools;
import com.github.pareronia.aoc.Pairwise;
import com.github.pareronia.aoc.Pairwise.Pair;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2025_09 extends SolutionBase<List<Cell>, Long, Long> {

    private AoC2025_09(final boolean debug) {
        super(debug);
    }

    public static AoC2025_09 create() {
        return new AoC2025_09(false);
    }

    public static AoC2025_09 createDebug() {
        return new AoC2025_09(true);
    }

    @Override
    protected List<Cell> parseInput(final List<String> inputs) {
        return inputs.stream()
                .map(
                        line -> {
                            final StringSplit split = StringOps.splitOnce(line, ",");
                            return Cell.at(
                                    Integer.parseInt(split.right()),
                                    Integer.parseInt(split.left()));
                        })
                .toList();
    }

    @Override
    public Long solvePart1(final List<Cell> reds) {
        return Utils.stream(IterTools.combinations(reds.size(), 2))
                .mapToLong(
                        c -> {
                            final long dr =
                                    Math.abs(reds.get(c[0]).getRow() - reds.get(c[1]).getRow()) + 1;
                            final long dc =
                                    Math.abs(reds.get(c[0]).getCol() - reds.get(c[1]).getCol()) + 1;
                            return dr * dc;
                        })
                .max()
                .getAsLong();
    }

    @Override
    public Long solvePart2(final List<Cell> reds) {
        final List<Pair<Cell>> borderSegments = new ArrayList<>();
        Pairwise.pairwise(reds.iterator()).forEachRemaining(borderSegments::add);
        borderSegments.add(Pair.of(reds.getLast(), reds.getFirst()));
        long ans = 0;
        for (int i = 0; i < reds.size(); i++) {
            for (int j = i + 1; j < reds.size(); j++) {
                final Cell celli = reds.get(i);
                final Cell cellj = reds.get(j);
                final int minRow = Math.min(celli.getRow(), cellj.getRow());
                final int maxRow = Math.max(celli.getRow(), cellj.getRow());
                final int minCol = Math.min(celli.getCol(), cellj.getCol());
                final int maxCol = Math.max(celli.getCol(), cellj.getCol());
                if (borderSegments.stream()
                        .allMatch(
                                pair ->
                                        Math.max(pair.first().getCol(), pair.second().getCol())
                                                        <= minCol
                                                || Math.min(
                                                                pair.first().getCol(),
                                                                pair.second().getCol())
                                                        >= maxCol
                                                || Math.max(
                                                                pair.first().getRow(),
                                                                pair.second().getRow())
                                                        <= minRow
                                                || Math.min(
                                                                pair.first().getRow(),
                                                                pair.second().getRow())
                                                        >= maxRow)) {
                    final long dc = maxCol - minCol + 1;
                    final long dr = maxRow - minRow + 1;
                    ans = Math.max(dc * dr, ans);
                }
            }
        }
        return ans;
    }

    @Samples({
        @Sample(method = "part1", input = TEST, expected = "50"),
        @Sample(method = "part2", input = TEST, expected = "24"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST =
            """
            7,1
            11,1
            11,7
            9,7
            9,5
            2,5
            2,3
            7,3
            """;
}
