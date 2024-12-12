import static com.github.pareronia.aoc.IntegerSequence.Range.range;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ToIntBiFunction;

import com.github.pareronia.aoc.Grid.Cell;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.geometry.Direction;
import com.github.pareronia.aoc.graph.BFS;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2024_12 extends SolutionBase<List<String>, Integer, Integer> {
    
    private static final List<List<Direction>> CORNER_DIRS = List.of(
        List.of(Direction.LEFT_AND_UP, Direction.LEFT, Direction.UP),
        List.of(Direction.RIGHT_AND_UP, Direction.RIGHT, Direction.UP),
        List.of(Direction.RIGHT_AND_DOWN, Direction.RIGHT, Direction.DOWN),
        List.of(Direction.LEFT_AND_DOWN, Direction.LEFT, Direction.DOWN)
    );

    private AoC2024_12(final boolean debug) {
        super(debug);
    }
    
    public static AoC2024_12 create() {
        return new AoC2024_12(false);
    }
    
    public static AoC2024_12 createDebug() {
        return new AoC2024_12(true);
    }
    
    @Override
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }
    
    private int solve(
            final List<String> input,
            final ToIntBiFunction<Cell, Set<Cell>> count
    ) {
        final Map<Character, Set<Cell>> plotsByPlant = new HashMap<>();
        range(input.size()).forEach(r ->
            range(input.get(r).length()).forEach(c ->
                plotsByPlant
                    .computeIfAbsent(input.get(r).charAt(c), k -> new HashSet<>())
                    .add(Cell.at(r, c))));
        final Iterator<Set<Cell>> regions = new Iterator<>() {
            final Iterator<Character> keys = plotsByPlant.keySet().iterator();
            char key = keys.next();
            Set<Cell> allPlotsWithPlant = plotsByPlant.get(key);

            @Override
            public Set<Cell> next() {
                if (allPlotsWithPlant.isEmpty()) {
                    key = keys.next();
                    allPlotsWithPlant = plotsByPlant.get(key);
                }
                final Set<Cell> region = BFS.floodFill(
                    allPlotsWithPlant.iterator().next(),
                    cell -> cell.capitalNeighbours()
                            .filter(allPlotsWithPlant::contains));
                allPlotsWithPlant.removeAll(region);
                return region;
            }
            
            @Override
            public boolean hasNext() {
                return !allPlotsWithPlant.isEmpty() || keys.hasNext();
            }
        };
        return Utils.stream(regions)
            .mapToInt(region -> region.stream()
                .mapToInt(plot -> count.applyAsInt(plot, region) * region.size())
                .sum())
            .sum();
    }
    
    @Override
    public Integer solvePart1(final List<String> input) {
        final ToIntBiFunction<Cell, Set<Cell>> countEdges
            = (plot, region) -> (4 - (int) plot.capitalNeighbours()
                    .filter(region::contains)
                    .count());
        return solve(input, countEdges);
    }
    
    @Override
    public Integer solvePart2(final List<String> input) {
        final Set<int[]> matches = Set.of(
            new int[] {0, 0, 0}, new int[] {1, 0, 0}, new int[] {0, 1, 1});
        final ToIntBiFunction<Cell, Set<Cell>> countCorners
            = (plot, region) -> ((int) CORNER_DIRS.stream()
                .filter(d -> {
                    final int[] test = range(3).intStream()
                        .map(i -> region.contains(plot.at(d.get(i))) ? 1 : 0)
                        .toArray();
                    return matches.stream().anyMatch(m -> Arrays.equals(m, test));
                })
                .count());
        return solve(input, countCorners);
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "140"),
        @Sample(method = "part1", input = TEST2, expected = "772"),
        @Sample(method = "part1", input = TEST3, expected = "1930"),
        @Sample(method = "part2", input = TEST1, expected = "80"),
        @Sample(method = "part2", input = TEST2, expected = "436"),
        @Sample(method = "part2", input = TEST3, expected = "1206"),
        @Sample(method = "part2", input = TEST4, expected = "236"),
        @Sample(method = "part2", input = TEST5, expected = "368"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2024_12.create().run();
    }

    private static final String TEST1 = """
            AAAA
            BBCD
            BBCC
            EEEC
            """;
    private static final String TEST2 = """
            OOOOO
            OXOXO
            OOOOO
            OXOXO
            OOOOO
            """;
    private static final String TEST3 = """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
            """;
    private static final String TEST4 = """
            EEEEE
            EXXXX
            EEEEE
            EXXXX
            EEEEE
            """;
    private static final String TEST5 = """
            AAAAAA
            AAABBA
            AAABBA
            ABBAAA
            ABBAAA
            AAAAAA
            """;
}