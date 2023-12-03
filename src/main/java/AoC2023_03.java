import static com.github.pareronia.aoc.Utils.enumerate;
import static java.util.stream.Collectors.groupingBy;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.Range;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2023_03
        extends SolutionBase<List<AoC2023_03.EnginePart>, Integer, Integer> {
    
    private AoC2023_03(final boolean debug) {
        super(debug);
    }
    
    public static AoC2023_03 create() {
        return new AoC2023_03(false);
    }
    
    public static AoC2023_03 createDebug() {
        return new AoC2023_03(true);
    }
    
    @Override
    protected List<EnginePart> parseInput(final List<String> inputs) {
        final Pattern regex = Pattern.compile("[0-9]+");
        final CharGrid grid = CharGrid.from(inputs);
        return enumerate(grid.getRowsAsStrings())
            .flatMap(e -> regex.matcher(e.getValue()).results()
                .map(m -> findEnginePart(grid, e.getIndex(),
                                         Range.between(m.start(), m.end()))))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }
    
    private Optional<EnginePart> findEnginePart(
            final CharGrid grid, final int row, final Range<Integer> colspan
    ) {
        return IntStream.range(colspan.getMinimum(), colspan.getMaximum())
            .boxed()
            .flatMap(col -> grid.getAllNeighbours(Cell.at(row, col)))
            .filter(n -> !Character.isDigit(grid.getValue(n)))
            .filter(n -> grid.getValue(n) != '.')
            .map(n -> {
                final String s = grid.getRowAsString(row)
                            .substring(colspan.getMinimum(), colspan.getMaximum());
                return new EnginePart(grid.getValue(n), Integer.parseInt(s), n);
            })
            .findFirst();
    }

    @Override
    public Integer solvePart1(final List<EnginePart> engineParts) {
        return engineParts.stream().mapToInt(EnginePart::number).sum();
    }
    
    @Override
    public Integer solvePart2(final List<EnginePart> engineParts) {
        return engineParts.stream()
            .filter(ep -> ep.part() == '*')
            .collect(groupingBy(EnginePart::pos))
            .values().stream()
            .filter(v -> v.size() == 2)
            .mapToInt(v -> v.get(0).number() * v.get(1).number())
            .sum();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST, expected = "4361"),
        @Sample(method = "part2", input = TEST, expected = "467835"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2023_03.create().run();
    }

    private static final String TEST = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
            """;
    
    record EnginePart(char part, int number, Cell pos) { }
}
