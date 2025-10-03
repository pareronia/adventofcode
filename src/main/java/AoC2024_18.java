import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.graph.BFS;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public final class AoC2024_18 extends SolutionBase<List<Cell>, Integer, String> {

    private static final int SIZE = 71;
    private static final int TIME = 1024;
    private static final Cell START = Cell.at(0, 0);

    private AoC2024_18(final boolean debug) {
        super(debug);
    }

    public static AoC2024_18 create() {
        return new AoC2024_18(false);
    }

    public static AoC2024_18 createDebug() {
        return new AoC2024_18(true);
    }

    @Override
    protected List<Cell> parseInput(final List<String> inputs) {
        return inputs.stream()
                .map(s -> StringOps.splitOnce(s, ","))
                .map(sp -> Cell.at(Integer.parseInt(sp.right()), Integer.parseInt(sp.left())))
                .toList();
    }

    private Stream<Cell> adjacent(final Cell cell, final int size, final Set<Cell> occupied) {
        return cell.capitalNeighbours()
                .filter(
                        n ->
                                0 <= n.getRow()
                                        && n.getRow() < size
                                        && 0 <= n.getCol()
                                        && n.getCol() < size
                                        && !occupied.contains(n));
    }

    private Integer solve1(final List<Cell> cells, final int size, final int time) {
        final Cell end = Cell.at(size - 1, size - 1);
        final Set<Cell> occupied = new HashSet<>(cells.subList(0, time));
        return BFS.execute(START, c -> c.equals(end), c -> adjacent(c, size, occupied));
    }

    private Set<Cell> free(final List<Cell> cells, final int size, final int time) {
        final Set<Cell> occupied = new HashSet<>(cells.subList(0, time));
        return BFS.floodFill(START, c -> adjacent(c, size, occupied));
    }

    private String solve2(final List<Cell> cells, final int size, final int time) {
        final Cell end = Cell.at(size - 1, size - 1);
        int lo = time;
        int hi = cells.size();
        while (lo < hi) {
            final int mid = (lo + hi) / 2;
            if (free(cells, size, mid).contains(end)) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        final Cell ans = cells.get(lo - 1);
        return String.format("%d,%d", ans.getCol(), ans.getRow());
    }

    @Override
    public Integer solvePart1(final List<Cell> input) {
        return solve1(input, SIZE, TIME);
    }

    @Override
    public String solvePart2(final List<Cell> input) {
        return solve2(input, SIZE, TIME);
    }

    @Override
    public void samples() {
        final AoC2024_18 test = AoC2024_18.createDebug();
        final List<Cell> input = test.parseInput(StringOps.splitLines(TEST));
        assert test.solve1(input, 7, 12) == 22;
        assert test.solve2(input, 7, 12).equals("6,1");
    }

    public static void main(final String[] args) throws Exception {
        AoC2024_18.create().run();
    }

    private static final String TEST =
            """
            5,4
            4,2
            4,5
            3,0
            2,1
            6,3
            2,4
            1,5
            0,6
            3,3
            2,6
            5,1
            1,2
            5,5
            2,5
            6,5
            1,4
            0,4
            6,4
            1,1
            6,1
            1,0
            0,5
            1,6
            2,0
            """;
}
