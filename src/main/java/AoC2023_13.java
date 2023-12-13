import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import java.util.List;
import java.util.stream.Stream;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.StringOps;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_13
        extends SolutionBase<List<CharGrid>, Integer, Integer> {
    
    private AoC2023_13(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_13 create() {
        return new AoC2023_13(false);
    }
    
    public static AoC2023_13 createDebug() {
        return new AoC2023_13(true);
    }
    
    @Override
    protected List<CharGrid> parseInput(final List<String> inputs) {
        return StringOps.toBlocks(inputs).stream().map(CharGrid::from).toList();
    }
    
    private Symmetry findSymmetry(final CharGrid grid, final int smudge) {
        return Stream.concat(
            range(grid.getWidth() - 1).intStream()
                .mapToObj(col -> {
                    final int errors = countHorizontalSymmetryErrors(grid, col);
                    return new Symmetry(0, col + 1, errors);
                })
                .filter(s -> s.errors() == smudge),
            range(grid.getHeight() - 1).intStream()
                .mapToObj(row -> {
                    final int errors = countVerticalSymmetryErrors(grid, row);
                    return new Symmetry(row + 1, 0, errors);
                })
                .filter(s -> s.errors() == smudge)
        ).findFirst().orElseThrow();
    }

    private int countVerticalSymmetryErrors(final CharGrid grid, final int row) {
        return range(grid.getHeight() / 2).intStream()
            .map(dr -> {
                final int up = row - dr;
                final int down = row + dr + 1;
                if (!(0 <= up && up < down && down < grid.getHeight())) {
                    return 0;
                }
                return (int) range(grid.getWidth()).intStream()
                    .filter(c -> grid.getValue(Cell.at(up, c)) != grid.getValue(Cell.at(down, c)))
                    .count();
            })
            .sum();
    }

    private int countHorizontalSymmetryErrors(final CharGrid grid, final int col) {
        return range(grid.getWidth() / 2).intStream()
            .map(dc -> {
                final int left = col - dc;
                final int right = col + dc + 1;
                if (!(0 <= left && left < right && right < grid.getWidth())) {
                    return 0;
                }
                return (int) range(grid.getHeight()).intStream()
                        .filter(r -> grid.getValue(Cell.at(r, left)) != grid.getValue(Cell.at(r, right)))
                        .count();
            })
            .sum();
    }
    
    private int solve(final List<CharGrid> grids, final int smudge) {
        return grids.stream()
            .map(g -> findSymmetry(g, smudge))
            .mapToInt(s -> 100 * s.row() + s.column())
            .sum();
    }

    @Override
    public Integer solvePart1(final List<CharGrid> grids) {
        return solve(grids, 0);
    }
    
    @Override
    public Integer solvePart2(final List<CharGrid> grids) {
        return solve(grids, 1);
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "405"),
        @Sample(method = "part2", input = TEST, expected = "400"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_13.create().run();
    }
    
    record Symmetry(int row, int column, int errors) {}

    private static final String TEST = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.

            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
            """;
}
