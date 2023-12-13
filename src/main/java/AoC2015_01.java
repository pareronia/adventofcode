import static com.github.pareronia.aoc.AssertUtils.unreachable;
import static com.github.pareronia.aoc.IterTools.enumerate;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public final class AoC2015_01
        extends SolutionBase<List<Direction>, Integer, Integer> {
    
    private AoC2015_01(final boolean debug) {
        super(debug);
    }
    
    public static AoC2015_01 create() {
        return new AoC2015_01(false);
    }
    
    public static AoC2015_01 createDebug() {
        return new AoC2015_01(true);
    }
    
    @Override
    protected List<Direction> parseInput(final List<String> inputs) {
        assert inputs.size() == 1;
        return Utils.asCharacterStream(inputs.get(0))
                .map(Direction::fromChar)
                .collect(toList());
    }

    @Override
    public Integer solvePart1(final List<Direction> input) {
        return input.stream().collect(Direction.summingInt());
    }
    
    @Override
    public Integer solvePart2(final List<Direction> input) {
        final Predicate<Direction> dropCondition = new Predicate<>() {
            private int sum = 0;
            
            @Override
            public boolean test(final Direction dir) {
                sum = dir.addTo(sum);
                return sum != -1;
            }
        };
        return enumerate(input.stream())
            .dropWhile(e -> dropCondition.test(e.value()))
            .map(e -> e.index() + 1)
            .findFirst().orElseThrow();
    }
    
    @Override
    @Samples({
        @Sample(method = "part1", input = TEST1, expected = "0"),
        @Sample(method = "part1", input = TEST2, expected = "0"),
        @Sample(method = "part1", input = TEST3, expected = "3"),
        @Sample(method = "part1", input = TEST4, expected = "3"),
        @Sample(method = "part1", input = TEST5, expected = "-1"),
        @Sample(method = "part1", input = TEST6, expected = "-1"),
        @Sample(method = "part1", input = TEST7, expected = "-3"),
        @Sample(method = "part1", input = TEST8, expected = "-3"),
        @Sample(method = "part2", input = TEST9, expected = "1"),
        @Sample(method = "part2", input = TEST10, expected = "5"),
    })
    public void samples() {
    }
    
    public static void main(final String[] args) throws Exception {
        AoC2015_01.create().run();
    }

    private static final String TEST1 = "(())";
    private static final String TEST2 = "()()";
    private static final String TEST3 = "(((";
    private static final String TEST4 = "))(((((";
    private static final String TEST5 = "())";
    private static final String TEST6 = "))(";
    private static final String TEST7 = ")))";
    private static final String TEST8 = ")())())";
    private static final String TEST9 = ")";
    private static final String TEST10 = "()())";
}
    
enum Direction {

    UP(1), DOWN(-1);
    
    private static final char UP_CHAR = '(';
    private static final char DOWN_CHAR = ')';
    
    private final int value;
    
    Direction(final int value) {
        this.value = value;
    }
    
    public static Direction fromChar(final Character ch) {
        return switch (ch) {
            case UP_CHAR -> Direction.UP;
            case DOWN_CHAR -> Direction.DOWN;
            default -> throw unreachable();
        };
    }
    
    public int addTo(final int lhs) {
        return lhs + this.value;
    }
    
    public static Collector<Direction, ?, Integer> summingInt() {
        return Collectors.summingInt(dir -> dir.value);
    }
}
