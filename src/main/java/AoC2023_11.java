import static com.github.pareronia.aoc.IntegerSequence.Range.range;
import static com.github.pareronia.aoc.StringOps.splitLines;
import static com.github.pareronia.aoc.itertools.IterTools.enumerate;
import static java.util.stream.Collectors.toSet;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.itertools.Enumerated;
import com.github.pareronia.aoc.solution.SolutionBase;

import java.util.List;
import java.util.Set;

public final class AoC2023_11
        extends SolutionBase<AoC2023_11.Observations, Long, Long> {
    
    private AoC2023_11(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_11 create() {
        return new AoC2023_11(false);
    }
    
    public static AoC2023_11 createDebug() {
        return new AoC2023_11(true);
    }
    
    @Override
    protected Observations parseInput(final List<String> inputs) {
        final CharGrid grid = CharGrid.from(inputs);
        final List<Cell> galaxies = grid.getAllEqualTo('#').toList();
        final Set<Integer> emptyRows = enumerate(grid.getRowsAsStrings()).stream()
            .filter(e -> !e.value().contains("#"))
            .map(Enumerated::index)
            .collect(toSet());
        final Set<Integer> emptyCols = enumerate(grid.getColumnsAsStrings()).stream()
            .filter(e -> !e.value().contains("#"))
            .map(Enumerated::index)
            .collect(toSet());
        return new Observations(galaxies, emptyRows, emptyCols);
    }
    
    private long solve(final Observations observations, final int factor) {
        return enumerate(observations.galaxies()).stream().flatMapToLong(e -> {
            final Cell g1 = e.value();
            return observations.galaxies().subList(
                    e.index(), observations.galaxies().size()).stream()
                .mapToLong(g2 -> {
                    final int minRow = Math.min(g1.getRow(), g2.getRow());
                    final int maxRow = Math.max(g1.getRow(), g2.getRow());
                    final long dr = range(minRow, maxRow, 1).intStream()
                        .map(r -> observations.emptyRows().contains(r) ? factor : 1)
                        .sum();
                    final int minCol = Math.min(g1.getCol(), g2.getCol());
                    final int maxCol = Math.max(g1.getCol(), g2.getCol());
                    final long dc = range(minCol, maxCol, 1).intStream()
                        .map(c -> observations.emptyCols().contains(c) ? factor : 1)
                        .sum();
                    return dr + dc;
                });
        }).sum();
    }

    @Override
    public Long solvePart1(final Observations observations) {
        return solve(observations, 2);
    }
    
    @Override
    public Long solvePart2(final Observations observations) {
        return solve(observations, 1_000_000);
    }
    
    @Override
    public void samples() {
        final AoC2023_11 test = AoC2023_11.createDebug();
        final AoC2023_11.Observations input = test.parseInput(splitLines(TEST));
        assert test.solve(input, 2) == 374;
        assert test.solve(input, 10) == 1030;
        assert test.solve(input, 100) == 8410;
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_11.create().run();
    }
    
    record Observations(List<Cell> galaxies, Set<Integer> emptyRows, Set<Integer> emptyCols) {}

    private static final String TEST = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
            """;
}
