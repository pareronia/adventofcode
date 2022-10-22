import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import com.github.pareronia.aoc.Grid;
import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Aocd;
import com.github.pareronia.aocd.Puzzle;

import lombok.Getter;

public final class AoC2017_21 extends AoCBase {
    
    private static final Grid START = Grid.from(List.of(".#.", "..#", "###"));

    private final List<Rule> rules;
    
    private AoC2017_21(final List<String> inputs, final boolean debug) {
        super(debug);
        this.rules = inputs.stream().map(this::parseRule).collect(toList());
    }

    private Rule parseRule(final String input) {
        final String[] splits = input.split(" => ");
        return new Rule(
            Grid.from(Arrays.asList(splits[0].split("/"))),
            Grid.from(Arrays.asList(splits[1].split("/"))));
    }

    public static AoC2017_21 create(final List<String> input) {
        return new AoC2017_21(input, false);
    }

    public static AoC2017_21 createDebug(final List<String> input) {
        return new AoC2017_21(input, true);
    }
    
    private Grid enhance(final Grid grid) {
        return this.rules.stream()
            .filter(r -> r.getFrom().contains(grid))
            .map(Rule::getTo)
            .findFirst().orElseThrow();
    }
    
    private long solve(final int iterations) {
        Grid grid = START;
        for (int i = 0; i < iterations; i++) {
            final int size = grid.getHeight();
            final Grid[][] subGrids = grid.divide(size % 2 == 0 ? 2 : 3);
            final Grid[][] enhanced = Arrays.stream(subGrids)
                .map(a ->
                    Arrays.stream(a).map(this::enhance).toArray(Grid[]::new))
                .toArray(Grid[][]::new);
            grid = Grid.merge(enhanced);
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
    
    @Getter
    private static final class Rule {
        private final List<Grid> from;
        private final Grid to;
        
        public Rule(final Grid from, final Grid to) {
            this.from = Utils.stream(from.getPermutations()).collect(toList());
            this.to = to;
        }
    }
}
