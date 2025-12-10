import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.StringOps.StringSplit;
import com.github.pareronia.aoc.itertools.IterTools;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.List;
import java.util.stream.Stream;

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
        return rectangles(reds).mapToLong(Rectangle::getArea).max().getAsLong();
    }

    @Override
    public Long solvePart2(final List<Cell> reds) {
        final List<Rectangle> borderSegments =
                IterTools.pairwise(Stream.concat(reds.stream(), Stream.of(reds.getFirst())))
                        .stream()
                        .map(pair -> Rectangle.from(pair.first(), pair.second()))
                        .toList();
        return rectangles(reds)
                .filter(rect -> borderSegments.stream().noneMatch(rect::intersects))
                .mapToLong(Rectangle::getArea)
                .max()
                .getAsLong();
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

    record Rectangle(int minRow, int minCol, int maxRow, int maxCol) {

        public static Rectangle from(final Cell first, final Cell second) {
            final int minRow = Math.min(first.getRow(), second.getRow());
            final int maxRow = Math.max(first.getRow(), second.getRow());
            final int minCol = Math.min(first.getCol(), second.getCol());
            final int maxCol = Math.max(first.getCol(), second.getCol());
            return new Rectangle(minRow, minCol, maxRow, maxCol);
        }

        public boolean intersects(final Rectangle other) {
            return !(this.maxCol <= other.minCol
                    || this.minCol >= other.maxCol
                    || this.maxRow <= other.minRow
                    || this.minRow >= other.maxRow);
        }

        public long getArea() {
            return (long) (maxRow - minRow + 1) * (maxCol - minCol + 1);
        }
    }

    private Stream<Rectangle> rectangles(final List<Cell> reds) {
        return Utils.stream(IterTools.combinations(reds.size(), 2))
                .map(combo -> Rectangle.from(reds.get(combo[0]), reds.get(combo[1])));
    }
}
