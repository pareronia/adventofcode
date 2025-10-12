import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.graph.Dijkstra;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings({"PMD.NoPackage", "PMD.ClassNamingConventions"})
public final class AoC2016_13 extends SolutionBase<Dijkstra.Result<Cell>, Integer, Integer> {

    private static final char ONE = '1';
    private static final Cell START = Cell.at(1, 1);

    private AoC2016_13(final boolean debug) {
        super(debug);
    }

    public static AoC2016_13 create() {
        return new AoC2016_13(false);
    }

    public static AoC2016_13 createDebug() {
        return new AoC2016_13(true);
    }

    @Override
    protected Dijkstra.Result<Cell> parseInput(final List<String> inputs) {
        final int input = Integer.parseInt(inputs.get(0));
        final Predicate<Cell> isOpenSpace =
                cell -> {
                    final int x = cell.getRow();
                    final int y = cell.getCol();
                    final int t = input + x * x + 3 * x + 2 * x * y + y + y * y;
                    final long ones =
                            Utils.asCharacterStream(Integer.toBinaryString(t))
                                    .filter(c -> c == ONE)
                                    .count();
                    return ones % 2 == 0;
                };
        return Dijkstra.execute(
                START,
                cell -> false,
                cell ->
                        cell.capitalNeighbours()
                                .filter(n -> n.getRow() >= 0 && n.getCol() >= 0)
                                .filter(isOpenSpace),
                (curr, next) -> 1);
    }

    private int getDistance(final Dijkstra.Result<Cell> input, final Cell end) {
        return (int) input.getDistance(end);
    }

    @Override
    public Integer solvePart1(final Dijkstra.Result<Cell> input) {
        return getDistance(input, Cell.at(31, 39));
    }

    @Override
    public Integer solvePart2(final Dijkstra.Result<Cell> input) {
        return (int) input.getDistances().values().stream().filter(v -> v <= 50).count();
    }

    @Override
    protected void samples() {
        final AoC2016_13 test = createDebug();
        final Dijkstra.Result<Cell> input = test.parseInput(StringOps.splitLines(TEST));
        assert test.getDistance(input, Cell.at(1, 1)) == 0;
        assert test.getDistance(input, Cell.at(7, 4)) == 11;
    }

    public static void main(final String[] args) throws Exception {
        create().run();
    }

    private static final String TEST = "10";
}
