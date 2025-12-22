import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.graph.BFS;
import com.github.pareronia.aoc.knothash.KnotHash;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.NoPackage"})
public final class AoC2017_14 extends SolutionBase<CharGrid, Long, Integer> {

    private static final char ON = '1';

    private AoC2017_14(final boolean debug) {
        super(debug);
    }

    public static AoC2017_14 create() {
        return new AoC2017_14(false);
    }

    public static AoC2017_14 createDebug() {
        return new AoC2017_14(true);
    }

    @Override
    protected CharGrid parseInput(final List<String> inputs) {
        return CharGrid.from(
                IntStream.range(0, 128)
                        .mapToObj(i -> inputs.getFirst() + "-" + i)
                        .map(KnotHash::binString)
                        .toList());
    }

    private Set<Cell> findRegion(final CharGrid grid, final Cell start) {
        final Function<Cell, Stream<Cell>> adjacent =
                cell -> grid.getCapitalNeighbours(cell).filter(n -> grid.getValue(n) == ON);
        return BFS.floodFill(start, adjacent);
    }

    @Override
    public Long solvePart1(final CharGrid grid) {
        return grid.getAllEqualTo(ON).count();
    }

    @Override
    public Integer solvePart2(final CharGrid grid) {
        final List<Set<Cell>> regions = new ArrayList<>();
        grid.getAllEqualTo(ON)
                .filter(c -> regions.stream().noneMatch(r -> r.contains(c)))
                .map(c -> findRegion(grid, c))
                .forEach(regions::add);
        return regions.size();
    }

    @Samples({
        @Sample(method = "part1", input = "flqrgnkx", expected = "8108"),
        @Sample(method = "part2", input = "flqrgnkx", expected = "1242"),
    })
    public static void main(final String[] args) throws Exception {
        create().run();
    }
}
