import static com.github.pareronia.aoc.Utils.toAString;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_04
        extends SolutionBase<CharGrid, Integer, Integer> {
    
    private AoC2024_04(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_04 create() {
        return new AoC2024_04(false);
    }
    
    public static AoC2024_04 createDebug() {
        return new AoC2024_04(true);
    }
    
    @Override
    protected CharGrid parseInput(final List<String> inputs) {
        return CharGrid.from(inputs);
    }
    
    @Override
    public Integer solvePart1(final CharGrid grid) {
        return (int) grid.getAllEqualTo('X')
            .flatMap(cell -> Direction.OCTANTS.stream()
                                .map(d -> grid.getCells(cell, d).iterator()))
            .filter(it -> Stream.of('M', 'A', 'S').allMatch(
                    ch -> it.hasNext() && grid.getValue(it.next()) == ch))
            .count();
    }
    
    @Override
    public Integer solvePart2(final CharGrid grid) {
        final List<Direction> dirs = List.of(
            Direction.LEFT_AND_UP,
            Direction.RIGHT_AND_DOWN,
            Direction.RIGHT_AND_UP,
            Direction.LEFT_AND_DOWN
        );
        final Set<String> matches = Set.of("MSMS", "SMSM", "MSSM", "SMMS");
        return (int) grid.getCellsWithoutBorder()
            .filter(cell -> grid.getValue(cell) == 'A')
            .filter(cell -> matches.contains(
                    dirs.stream()
                        .map(d -> grid.getValue(cell.at(d)))
                        .collect(toAString())))
            .count();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "18"),
        @Sample(method = "part2", input = TEST, expected = "9"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_04.create().run();
    }

    private static final String TEST = """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
            """;
}