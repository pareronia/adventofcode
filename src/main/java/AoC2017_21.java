import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import com.github.pareronia.aoc.CharGrid;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

public final class AoC2017_21 extends AoCBase {
    
    private static final CharGrid START = CharGrid.from(List.of(".#.", "..#", "###"));

    private final List<Rule> rules;
    
    private AoC2017_21(final List<String> inputs, final boolean debug) {
        super(debug);
        this.rules = inputs.stream().map(this::parseRule).collect(toList());
    }

    private Rule parseRule(final String input) {
        final String[] splits = input.split(" => ");
        return new Rule(
            CharGrid.from(Arrays.asList(splits[0].split("/"))),
            CharGrid.from(Arrays.asList(splits[1].split("/"))));
    }

    public static AoC2017_21 create(final List<String> input) {
        return new AoC2017_21(input, false);
    }

    public static AoC2017_21 createDebug(final List<String> input) {
        return new AoC2017_21(input, true);
    }
    
    private CharGrid enhance(final CharGrid grid) {
        return this.rules.stream()
            .filter(r -> r.from().contains(grid))
            .map(Rule::to)
            .findFirst().orElseThrow();
    }
    
    private long solve(final int iterations) {
        CharGrid grid = START;
        for (int i = 0; i < iterations; i++) {
            final int size = grid.getHeight();
            final CharGrid[][] subGrids = grid.divide(size % 2 == 0 ? 2 : 3);
            final CharGrid[][] enhanced = Arrays.stream(subGrids)
                .map(a ->
                    Arrays.stream(a).map(this::enhance).toArray(CharGrid[]::new))
                .toArray(CharGrid[][]::new);
            grid = CharGrid.merge(enhanced);
        }
        return grid.countAllEqualTo('#');
    }

    @Override
    public Long solvePart1() {
        return solve(5);
    }
    
    @Override
    public Long solvePart2() {
        return solve(18);
    }

    public static void main(final String[] args) throws Exception {
        assert AoC2017_21.createDebug(TEST).solve(2) == 12L;

        final Puzzle puzzle = Aocd.puzzle(2017, 21);
        final List<String> input = puzzle.getInputData();
        puzzle.check(
            () -> lap("Part 1", create(input)::solvePart1),
            () -> lap("Part 2", create(input)::solvePart2)
        );
    }
    
    private static final List<String> TEST = splitLines(
        "../.# => ##./#../...\r\n" +
        ".#./..#/### => #..#/..../..../#..#"
    );
    
    record Rule(List<CharGrid> from, CharGrid to) {
        
        public Rule(final CharGrid from, final CharGrid to) {
            this(Utils.stream(from.getPermutations()).collect(toList()), to);
        }
    }
}
